'use strict';
import { ErrorTypes } from "./errorTypes";
import { NotificationType } from "./notificationsType";
import { FirestoreConst } from "./firestoreConst";
const dotenv = require('dotenv').config();
if (dotenv.error) {
  throw dotenv.error
}
console.log(dotenv.parsed)

const functions = require('firebase-functions');
const admin = require('firebase-admin');
// const serviceAccount = require('./services.json');
const fs = require('fs')
let serviceAccount = JSON.parse(fs.readFileSync('services.json', 'utf-8'))
admin.initializeApp({
  credential: admin.credential.cert(serviceAccount),
  databaseURL: "https://listopia-f7bba.firebaseio.com"
});
const express = require('express');
const bodyParser = require('body-parser')
const cookieParser = require('cookie-parser')();
const cors = require('cors')({origin: true});
const app = express();
const http = require('http');
const server = http.createServer(app);

const db = admin.firestore();

// Express middleware that validates Firebase ID Tokens passed in the Authorization HTTP header.
// The Firebase ID token needs to be passed as a Bearer token in the Authorization HTTP header like this:
// `Authorization: Bearer <Firebase ID Token>`.
// when decoded successfully, the ID Token content will be added as `req.user`.
const validateFirebaseIdToken = async (req, res, next) => {
  //console.log('Check if request is authorized with Firebase ID token');

  if ((!req.headers.authorization || !req.headers.authorization.startsWith('Bearer ')) &&
      !(req.cookies && req.cookies.__session)) {
    console.error('No Firebase ID token was passed as a Bearer token in the Authorization header.',
        'Make sure you authorize your request by providing the following HTTP header:',
        'Authorization: Bearer <Firebase ID Token>',
        'or by passing a "__session" cookie.');
    res.status(401).send(parseError("Unauthorized", "TokenError", "There is no token!"));
    return;
  }

  let idToken;
  if (req.headers.authorization && req.headers.authorization.startsWith('Bearer ')) {
    //console.log('Found "Authorization" header');
    // Read the ID Token from the Authorization header.
    idToken = req.headers.authorization.split('Bearer ')[1];
  } else if(req.cookies) {
    console.log('Found "__session" cookie');
    // Read the ID Token from cookie.
    idToken = req.cookies.__session;
  } else {
    // No cookie
    console.log("No cookie ERROR");
    res.status(401).send('Unauthorized');
    return;
  }

  try {
    const decodedIdToken = await admin.auth().verifyIdToken(idToken);
    //console.log('ID Token correctly decoded', decodedIdToken);
    req.user = decodedIdToken;
    next();
    return;
  } catch (error) {
    console.error('Error while verifying Firebase ID token:', error);
    res.status(401).send(parseError("Unauthorized"));
    return;
  }
};

app.use(cors);
app.use(cookieParser);
app.disable('etag');
app.use(validateFirebaseIdToken);
app.use(bodyParser.urlencoded({
  extended: true
}));
app.use(bodyParser.json());
app.get('/hello', (req, res) => {
  const fbtest = process.env.FIREBASE_CONFIG
  const tet = process.env.EXAMPLE_VAR
  const testObject = {
    test: "Test3"
  }
   console.error('Success', "Yey");
  res.json(testObject);
});

app.get('/user/get/:userId', (req, res) => {
  console.log("CALL: /user/get/:userId");
  var userRef = db.collection(FirestoreConst.USERS).doc(req.params.userId)
  userRef.get().then(function(doc) {
    if (doc.exists) {
      var friendsRef = []
      var user = doc.data()
      user.friends.forEach(function(id) {
        friendsRef.push(db.collection(FirestoreConst.USERS).doc(id))
      })
      var allFriends = []
      if (friendsRef.length != 0) {
        db.getAll(...friendsRef).then(docs => {
          docs.forEach(function(friend) {
            var friendData = friend.data();
            delete friendData.friends;
            allFriends.push(friendData)
          })
          user.friends = allFriends
          res.status(200).json(user);
        })     
      } else {
        user.friends = allFriends
        res.status(200).json(user);
      }  
    } else {
      res.status(404).json({
        code: 404,
        message: "User not found"
      });
    }
  }).catch(error => {
    console.log(error)
    res.status(500).send(parseError("Failed to add user into Firestore"));
  });
});

app.delete('/user/:userId/delete-friend/:friendId', (req, res) => {
  var userRef = db.collection(FirestoreConst.USERS).doc(req.params.userId)
  userRef.update({
    "friends": admin.firestore.FieldValue.arrayRemove(req.params.friendId)
  }).catch(error => {
    res.status(500).send(parseError("Failed to delete friend" + req.params.friendId));
  })
  var friendRef = db.collection(FirestoreConst.USERS).doc(req.params.friendId)
  friendRef.update({
    "friends": admin.firestore.FieldValue.arrayRemove(req.params.userId)
  }).catch(error => {
    res.status(500).send(parseError("Failed to delete friend of other" + req.params.userId));
  })
  res.status(200).json({});
});

app.post('/user/save', (req, res) => {
  console.log("CALL: /user/save");
  var userRef = db.collection(FirestoreConst.USERS).doc(req.body.id)
  userRef.get().then(function(thisDoc) {
    if (thisDoc.exists) {
      var updatedUser = {
        avatar: req.user.picture,
        name: req.user.name,
        accessToken: req.user
      };
      userRef.update(updatedUser)
      console.log('Updated user document with ID: ', req.body.id);
    } else {
      var newUser = {
        id: req.body.id,
        avatar: req.body.avatar,
        name: req.body.name,
        accessToken: req.user,
        firebaseToken: "",
        friends: []
      };
      userRef.set(newUser)
      console.log('Added user document with ID: ', req.body.id);
    }
    res.status(201).json({});
  }).catch(error => {
    console.log(error)
    res.status(500).send(parseError("Failed to add user into Firestore"));
  });
});

app.post('/user/:userId/add-friend', (req, res) => {
  var friendRef = db.collection(FirestoreConst.USERS).doc(req.body.friendId)
  friendRef.get().then(function(thisDoc) {
    if (thisDoc.exists) {
      friendRef.update({
        friends: admin.firestore.FieldValue.arrayUnion(req.params.userId)
      })
      console.log('Updated user document with ID: ', req.body.friendId);

      var userRef = db.collection(FirestoreConst.USERS).doc(req.params.userId)
      userRef.update({
        friends: admin.firestore.FieldValue.arrayUnion(req.body.friendId)
      }).then(function() {
        console.log('Successfully added friend with ID: ', req.body.friendId);
        var user = thisDoc.data()
        user.friends = null
        res.status(201).send(user);
      }).catch(error => {
        console.log(error)
        res.status(500).send(parseError("Failed to add friend into Firestore"));
      })
    } else {
      //TODO: Send email address to friend - invatation with a link to playstore
      // var newUser = {
      //   avatar: null,
      //   name: null,
      //   friends: [req.params.userId]
      // };
      // friendRef.set(newUser)
      // console.log('Added user document with ID: ', req.body.friendId);
      
      res.status(404).send(parseError("User does not exists in system", ErrorTypes.USER_DOES_NOT_EXISTS));
    }
  }).catch(error => {
    console.log(error)
    res.status(500).send(parseError("Failed to add friend into Firestore"));
  });
  
});


app.post('/user/friends', (req, res) => {
  try {
    var friendsRef = [];
    var friendsIds = req.body.friendsId;
    friendsIds.forEach(function (friendId) {
        friendsRef.push(db.collection(FirestoreConst.USERS).doc(friendId));
    });
    getAllFriends(res, friendsRef).then().catch();
  }catch(error) {
    res.status(500).send(parseError("Failed to fetch"));
  };
});

async function getAllFriends(res: any, friendsRef: any) {
  var allFriends = [];
  if (friendsRef.length != 0) {
    var friendsDocuments = await getFriends(friendsRef)
    for (let friendDocument of friendsDocuments) {
      var friendData = friendDocument.data();
      friendData.friends = null
      allFriends.push(friendData)
    }
    console.log(allFriends)
    res.status(200).json(allFriends);
  } else {
    console.log(allFriends)
    res.status(200).json(allFriends);
  }  
}

async function getFriends(friendsRef: any) {
  return db.getAll(...friendsRef)
}

app.put('/user/firebase', (req, res) => {
  console.log("CALL: /user/firebase");
  var userRef = db.collection(FirestoreConst.USERS).doc(req.body.userId)
  userRef.update({
    firebaseToken: req.body.token
  }).then(function() {
    res.status(201).json({});
  }).catch(error => {
    console.log(error)
    res.status(500).send(parseError("Failed to update firebase token into Firestore with ID: " + req.body.userId));
  });
});

app.post('/shopping-list/add-editor', (req, res) => {
  var shoppingListRef = db.collection(FirestoreConst.SHOPPING_LIST).doc(req.body.shoppingListId)
  shoppingListRef.get().then(function(shoppingList) {
    if (shoppingList.exists) {
      shoppingListRef.update({
        timestamp: req.body.timestamp,
        editors: admin.firestore.FieldValue.arrayUnion(req.body.editorId)
      }).then(function() {
        var payload = {
          data: {
            notification: NotificationType.SHOPPING_LIST_UPDATED,
            shoppingListId: req.body.shoppingListId
          }
        };
        sendFCMtoEditors(req.user.email, req.body.shoppingListId, payload).then().catch();
  
        console.log('Added editor with ID: ', req.body.editorId);
        res.status(201).json({});
      }).catch(error => {
        res.status(500).send(parseError("Failed to add editor into Firestore"));
      });
    } else {
      res.status(404).send(parseError("ShoppingList does not exists in database with ID: " + req.body.shoppingListId));
    }
  }).catch(error => {
    console.log(error)
    res.status(500).send(parseError("Failed to add editor into Firestore"));
  });
});

app.put('/shopping-list/delete-editor', (req, res) => {
  var shoppingListRef = db.collection(FirestoreConst.SHOPPING_LIST).doc(req.body.shoppingListId)
  var payload = {
    data: {
      notification: NotificationType.SHOPPING_LIST_UPDATED,
      shoppingListId: req.body.shoppingListId
    }
  };
  sendFCMtoEditors(req.user.email, req.body.shoppingListId, payload).then().catch();
  shoppingListRef.get().then(function(shoppingList) {
    if (shoppingList.exists) {
      shoppingListRef.update({
        timestamp: req.body.timestamp,
        editors: admin.firestore.FieldValue.arrayRemove(req.body.editorId)
      }).then(function() {
        // var payload = {
        //   data: {
        //     notification: NotificationType.SHOPPING_LIST_UPDATED,
        //     shoppingListId: req.body.shoppingListId
        //   }
        // };
        // sendFCMtoEditors(req.user.email, req.body.shoppingListId, payload);
  
        console.log('Deleted editor with ID: ', req.body.editorId);
        res.status(201).json({});
      }).catch(error => {
        res.status(500).send(parseError("Failed to delete editor from Firestore"));
      });
    } else {
      res.status(404).send(parseError("ShoppingList does not exists in database with ID: " + req.body.shoppingListId));
    }
  }).catch(error => {
    console.log(error)
    res.status(500).send(parseError("Failed to delete editor from Firestore"));
  });
});

app.post('/shopping-list/add', (req, res) => {
  var newShoppingList = req.body
  newShoppingList.editors.push(req.user.email)
  var shoppingListRef = db.collection(FirestoreConst.SHOPPING_LIST).doc(newShoppingList.id)
  shoppingListRef.set(newShoppingList).then(function() {
    res.status(201).json({});
  }).catch(error => {
    res.status(500).send(parseError("Failed to add shopping list into Firestore with ID: " + req.body.id));
  });
});

app.put('/shopping-list/update', (req, res) => {
  var shoppingListRef = db.collection(FirestoreConst.SHOPPING_LIST).doc(req.body.id)
  shoppingListRef.update(req.body).then(function() {
    var payload = {
      data: {
        notification: NotificationType.SHOPPING_LIST_UPDATED,
        shoppingListId: req.body.id
      }
    };
    sendFCMtoEditors(req.user.email, req.body.id, payload).then().catch();
    res.status(201).json({});
  }).catch(error => {
    res.status(500).send(parseError("Failed to update shopping list into Firestore with ID: " + req.body.id));
  });
});

app.post('/shopping-list/save-update', (req, res) => {
  console.log("CALL: /shopping-list/save-update")
  var shoppingLists = req.body;
  let batch = db.batch();
  shoppingLists.forEach(shoppingList => {
    if (shoppingList.editors.length != 0) {
      var editorsIds = [];
      shoppingList.editors.forEach(element => {
        editorsIds.push(element.id);
      });
      shoppingList.editors = editorsIds;
    }
    batch.set(db.collection(FirestoreConst.SHOPPING_LIST).doc(shoppingList.id), shoppingList, {merge: true})
  });
  batch.commit().then(function() {
    shoppingLists.forEach(shoppingList => {
      var payload = {
        data: {
          notification: NotificationType.SHOPPING_LIST_UPDATED,
          shoppingListId: shoppingList.id
        }
      };
      sendFCMtoEditors(req.user.email, shoppingList.id, payload).then().catch();
    });
    res.status(201).json({});
  }).catch(error => {
    res.status(500).send(parseError("Failed to add/update shopping lists into Firestore"));
  });
});

app.post('/product/save-update', (req, res) => {
  console.log("CALL: /product/save-update")
  var products = req.body;
  let batch = db.batch();
  var shoppingListIds = [];
  products.forEach(product => {
    shoppingListIds.push(product.shoppingListId)
    batch.set(db.collection(FirestoreConst.SHOPPING_LIST).doc(product.shoppingListId).collection(FirestoreConst.PRODUCTS).doc(product.id), product, {merge: true})
  });
  batch.commit().then(function() {
    //var shoppingListIdsUniqe = shoppingListIds.filter( onlyUnique );
    products.forEach(product => {
      var payload = {
        data: {
          notification: NotificationType.PRODUCT_UPDATED,
          productId: product.id,
          shoppingListId: product.shoppingListId
        }
      };
      sendFCMtoEditors(req.user.email, product.shoppingListId, payload).then().catch();
    });
    res.status(201).json({});
  }).catch(error => {
    res.status(500).send(parseError("Failed to add/update products into Firestore"));
  });
});

function onlyUnique(value, index, self) { 
  return self.indexOf(value) === index;
}

async function sendFCM(userId, payload) {
  var options = {
    priority: 'high',
    timeToLive: 60 * 60 * 24
  };
  db.collection(FirestoreConst.USERS).doc(userId).get().then(document => {
    if (document.empty) {
      console.log("No matching user");
      return
    }
    var token = document.data().firebaseToken;

    admin.messaging().sendToDevice(token, payload, options)
    .then((response) => {
      console.log('Successfully sent message:', response);
    })
    .catch((error) => {
      console.log('Error sending message:', error);
    });
  });
}

async function sendFCMtoEditors(senderId: string, shoppingListId: string, payload: any) {
  db.collection(FirestoreConst.SHOPPING_LIST).doc(shoppingListId).get().then(document => {
    if (document.empty) {
      console.log("No matching shopping list");
      return
    }
    var editors = document.data().editors
    //editors.push(document.data().ownerId)
    const index = editors.indexOf(senderId, 0);
    if (index > -1) {
      editors.splice(index, 1);
    }

    var editorsRef = [];
    editors.forEach(function(id) {
      editorsRef.push(db.collection(FirestoreConst.USERS).doc(id));
    });
    getEditorsTokens(editorsRef, payload).then().catch();
  });
}

async function getEditorsTokens(editorsRef: any, payload: any) {
  var allTokens = [];
  if (editorsRef.length != 0) {
    let editors = await getEditos(editorsRef)
    for (let editor of editors) {
        var editorData = editor.data();
        allTokens.push(editorData.firebaseToken)
    }
    var options = {
      priority: 'high',
      timeToLive: 60 * 60 * 24
    };
    admin.messaging().sendToDevice(allTokens, payload, options)
      .then((response) => {
        console.log('Successfully sent message:', response);
      })
      .catch((error) => {
        console.log('Error sending message:', error);
      });
  } 
}

app.delete('/shopping-list/delete/:shoppingListId', (req, res) => {
  try {
    const shoppingListProductsCollection = db.collection(FirestoreConst.SHOPPING_LIST).doc(req.params.shoppingListId).collection(FirestoreConst.PRODUCTS)
    var deleted = new Promise((resolve, reject) => {
      deleteQueryBatch(shoppingListProductsCollection, 10, resolve, reject)
    }).then(function() {
      db.collection(FirestoreConst.SHOPPING_LIST).doc(req.params.shoppingListId).delete();
      res.status(200).json({});
    }).catch(error => {
      res.status(500).send(parseError("Failed to delete shopping list" + req.params.shoppingListId));
    });
  }catch(error) {
    res.status(500).send(parseError("Failed to delete shopping list" + req.params.shoppingListId));
  };
});

app.delete('/product/delete/:shoppingListId/:productId', (req, res) => {
  try {
    db.collection(FirestoreConst.SHOPPING_LIST).doc(req.params.shoppingListId).collection(FirestoreConst.PRODUCTS).doc(req.params.productId).delete();
    res.status(200).json({});
  }catch(error) {
    res.status(500).send(parseError("Failed to delete product" + req.params.productId));
  };
});

function deleteQueryBatch(query, batchSize, resolve, reject) {
  query.get()
    .then((snapshot) => {
      // When there are no documents left, we are done
      if (snapshot.size == 0) {
        return 0;
      }

      // Delete documents in a batch
      let batch = db.batch();
      snapshot.docs.forEach((doc) => {
        batch.delete(doc.ref);
      });

      return batch.commit().then(() => {
        return snapshot.size;
      });
    }).then((numDeleted) => {
      if (numDeleted === 0) {
        resolve();
        return;
      }

      // Recurse on the next process tick, to avoid
      // exploding the stack.
      process.nextTick(() => {
        deleteQueryBatch(query, batchSize, resolve, reject);
      });
    })
    .catch(reject);
}

app.put('/product/update', (req, res) => {
  var productRef = db.collection(FirestoreConst.SHOPPING_LIST).doc(req.body.shoppingListId).collection(FirestoreConst.PRODUCTS).doc(req.body.id)
  productRef.update(req.body).then(function() {
    var payload = {
      data: {
        notification: NotificationType.PRODUCT_UPDATED,
        productId: req.body.id,
        shoppingListId: req.body.shoppingListId
      }
    };
    sendFCMtoEditors(req.user.email, req.body.shoppingListId, payload).then().catch();
    res.status(201).json({});
  }).catch(error => {
    res.status(500).send(parseError("Failed to update product into Firestore with ID: " + req.body.id));
  });
});

app.post('/product/add', (req, res) => {
  var productRef = db.collection(FirestoreConst.SHOPPING_LIST).doc(req.body.shoppingListId).collection(FirestoreConst.PRODUCTS).doc(req.body.id)
  productRef.set(req.body).then(function() {
    var payload = {
      data: {
        notification: NotificationType.PRODUCT_UPDATED,
        productId: req.body.id,
        shoppingListId: req.body.shoppingListId
      }
    };
    sendFCMtoEditors(req.user.email, req.body.shoppingListId, payload).then().catch();
    res.status(201).json({});
  }).catch(error => {
    res.status(500).send(parseError("Failed to add product into Firestore with ID: " + req.body.id));
  });
});

app.post('/product/get-all', (req, res) => {
  try {
    var products = [];
    var shoppingListids = req.body.shoppingListsId
    shoppingListids.forEach(function (shoppingListId, i) {
      db.collection(FirestoreConst.SHOPPING_LIST).doc(shoppingListId).get().then(document => {
        if (document.empty) {
          console.log("No matching documents for shoppings lists to fetch products");
          res.status(204).json({});
          return
        }
        if (i == shoppingListids.length - 1) {
          getProducts(req, res, document, products, true).then().catch();
        } else {
          getProducts(req, res, document, products, false).then().catch();
        }
      });
    }); 
  }catch(error) {
    res.status(500).send(parseError("Failed to fetch"));
  };
});

app.get('/shopping-list/:shoppingListId/product/:productId', (req, res) => {
  try {
    var productRef = db.collection(FirestoreConst.SHOPPING_LIST).doc(req.params.shoppingListId).collection(FirestoreConst.PRODUCTS).doc(req.params.productId)
    productRef.get().then(document => {
      if (document.empty) {
        console.log("No matching document for product");
        res.status(204).json({});
        return
      }
      var product = document.data();
      res.status(200).json(product);
    });
  }catch(error) {
    res.status(500).send(parseError("Failed to fetch"));
  };
});

 async function getShopingingList(req: any, res: any, products: any, shoppingListId: any, isLastItem: boolean) {
  db.collection(FirestoreConst.SHOPPING_LIST).doc(shoppingListId).get().then(document => {
    if (document.empty) {
      console.log("No matching documents for shoppings lists to fetch products");
      res.status(204).json({});
      return
    }
    getProducts(req, res, document, products, isLastItem).then().catch();
  });
}

async function getProducts(req: any, res: any, document: any, products: any, isLastItem: boolean) {
 document.ref.collection(FirestoreConst.PRODUCTS).get().then((querySnapshot) => {
    querySnapshot.forEach((productDocument) => {
      products.push(productDocument.data())
    });
    if (isLastItem) {
      res.status(200).json(products);
    }
  });
}

app.get('/shopping-list/get/:shoppingListId', (req, res) => {
  db.collection(FirestoreConst.SHOPPING_LIST).doc(req.params.shoppingListId).get().then(document => {
      if (document.empty) {
        console.log("No matching document for shoppings list");
        res.status(204).json({});
        return
      }
      getShoppingList(document, res).then().catch();
    }).catch(error => {
      res.status(500).send(parseError("Failed to get shopping list Firestore"));
    });
});

async function getShoppingList(document: any, res: any) {
  var shopingList = document.data();
  var editorsRef = [];
  shopingList.editors.forEach(function(id) {
    editorsRef.push(db.collection(FirestoreConst.USERS).doc(id));
  });
  await getShoppingListWithEditors(editorsRef, shopingList)
  res.status(200).json(shopingList);
}

async function getShoppingListWithEditors(editorsRef: any, shopingList: any) {
  var allEditors = [];
  if (editorsRef.length != 0) {
    let editors = await getEditos(editorsRef)
    for (let editor of editors) {
        var editorData = editor.data();
        delete editorData.friends;
        allEditors.push(editorData)
    }
    shopingList.editors = allEditors
  } else {
    shopingList.editors = allEditors
  }  
}

app.get('/shopping-list/get-all/:userId', (req, res) => {
  console.log("CALL: /shopping-list/get-all/:userId");
  try {
    var shoppingListsRef = db.collection(FirestoreConst.SHOPPING_LIST);
    var promise1 = shoppingListsRef.where('ownerId', '==', req.params.userId).get()
    var promise2 = shoppingListsRef.where('editors', 'array-contains', req.params.userId).get()

    Promise.all( [ promise1, promise2 ] ).then(function(values) {
      var combinedSnapshots = [];
      combinedSnapshots = values[0].docs.concat(values[1].docs)
      if (combinedSnapshots.length == 0) {
        console.log("No matching documents for shoppings lists");
        res.status(204).json({});
        return
      }
      var shoppingLists = getShoppingLists(combinedSnapshots, res)
    }).catch();
  }catch(error) {
    res.status(500).send(parseError("Failed to get shopping lists Firestore"));
  };
});

async function getShoppingLists(combinedSnapshots: any, res: any) {
    var shoppingLists = [];
    for (let snap of combinedSnapshots) {
      var shopingList = snap.data();
      var editorsRef = [];
      shopingList.editors.forEach(function(id) {
        editorsRef.push(db.collection(FirestoreConst.USERS).doc(id));
      });
      await getShoppingListsWithEditors(editorsRef, shopingList, shoppingLists)
    };
    res.status(200).json(shoppingLists);
    return shoppingLists
}

async function getShoppingListsWithEditors(editorsRef: any, shopingList: any, shoppingLists: any) {
  var allEditors = [];
  if (editorsRef.length != 0) {
      let editors = await getEditos(editorsRef)
      for (let editor of editors) {
          var editorData = editor.data();
          delete editorData.friends;
          allEditors.push(editorData)
      }
      shopingList.editors = allEditors
      shoppingLists.push(shopingList)
  } else {
    shopingList.editors = allEditors
    shoppingLists.push(shopingList)
  }  
}

function getEditos(editorsRef: any) {
  return db.getAll(...editorsRef)
}

app.get('/generate-sample-data', (req, res) => {
  var user1 = {
    id: 'qqqqqqqqq111111111',
    name: 'Pera Peric',
    email: 'pera_peric@testmail.com',
    avatar: 'https://lh6.googleusercontent.com/-ZHu0DHyICb0/AAAAAAAAAAI/AAAAAAAAAGA/nTs_D1FNIzk/s96-c/photo.jpg',
    friends: ['wwwwwwwwwwww22222222', 'eeeeeeeeeee3333333333']
  }

  var user2 = {
    id: 'wwwwwwwwwwww22222222',
    name: 'Ana Anic',
    email: 'ana_anic@testmail.com',
    avatar: 'https://lh6.googleusercontent.com/-ZHu0DHyICb0/wwwwwwww/222222222/nTs_D1FNIzk/s96-c/photo.jpg',
    friends: ['eeeeeeeeeee3333333333']
  }

  var user3 = {
    id: 'eeeeeeeeeee3333333333',
    name: 'Milan Milance',
    email: 'milan_milance@testmail.com',
    avatar: 'https://lh6.googleusercontent.com/-ZHu0DHyICb0/eeeeeee/3333333/nTs_D1FNIzk/s96-c/photo.jpg',
    friends: []
  }

  var shoppingList1 = {
    id: '111',
    name: 'Party',
    ownerId: 'qqqqqqqqq111111111',
    editors: ['wwwwwwwwwwww22222222', 'eeeeeeeeeee3333333333']
  }

  var shoppingList2 = {
    id: '222',
    name: 'Cake',
    ownerId: 'qqqqqqqqq111111111',
    editors: ['wwwwwwwwwwww22222222']
  }

  var product1 = {
    id: '111111111',
    shoppingListId: '111',
    name: 'Ballons',
    quantity: 10,
    unit: 'piece',
    price: 189,
    notes: 'Black ballons only',
    isChecked: false
  }

  var product2 = {
    id: '222222222',
    shoppingListId: '111',
    name: 'Beers',
    quantity: 20,
    unit: 'piece',
    price: 55,
    notes: 'Lasko if not then Jelen',
    isChecked: true
  }

  var product3 = {
    id: '333333333',
    shoppingListId: '222',
    name: 'Eggs',
    quantity: 5,
    unit: 'piece',
    price: 13,
    notes: 'From Chichken, medium size',
    isChecked: false
  }

  var batch = db.batch();

  batch.set(db.collection(FirestoreConst.USERS).doc('qqqqqqqqq111111111'), user1);
  batch.set(db.collection(FirestoreConst.USERS).doc('wwwwwwwwwwww22222222'), user2);
  batch.set(db.collection(FirestoreConst.USERS).doc('eeeeeeeeeee3333333333'), user3);
  batch.set(db.collection(FirestoreConst.SHOPPING_LIST).doc('111'), shoppingList1);
  batch.set(db.collection(FirestoreConst.SHOPPING_LIST).doc('222'), shoppingList2);
  batch.set(db.collection(FirestoreConst.SHOPPING_LIST).doc('111').collection(FirestoreConst.PRODUCTS).doc('111111111'), product1);
  batch.set(db.collection(FirestoreConst.SHOPPING_LIST).doc('111').collection(FirestoreConst.PRODUCTS).doc('222222222'), product2);
  batch.set(db.collection(FirestoreConst.SHOPPING_LIST).doc('222').collection(FirestoreConst.PRODUCTS).doc('333333333'), product3);

  batch.commit().then(function () {
    const testObject = {
      test: "Success"
    }
    res.json(testObject);
  }).catch(error => {
    console.error("Error generate data ", error);
  });
});

interface ErrorResponse {
  message: String;
  errorType: String;
  data: String;
}

function parseError(text: String = null, errorType: String = null, data: String = null) : ErrorResponse {
  return {
      message: text,
      errorType: errorType,
      data: data
  };
};

// This HTTPS endpoint can only be accessed by your Firebase Users.
// Requests need to be authorized by providing an `Authorization` HTTP header
// with value `Bearer <Firebase ID Token>`.
exports.app = functions.https.onRequest(app);
