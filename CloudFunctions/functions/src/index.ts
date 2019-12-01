'use strict';
import { ErrorType } from "./errorType";
const dotenv = require('dotenv').config();
if (dotenv.error) {
  throw dotenv.error
}
console.log(dotenv.parsed)

const functions = require('firebase-functions');
const admin = require('firebase-admin');
admin.initializeApp();
const express = require('express');
const bodyParser = require('body-parser')
const cookieParser = require('cookie-parser')();
const cors = require('cors')({origin: true});
const app = express();
const http = require('http');
const server = http.createServer(app);

// server.listen(5000, 'localhost');
// server.on('listening', function() {
//     console.log('Express server started on port %s at %s', server.address().port, server.address().address);
// });

const db = admin.firestore();

// Express middleware that validates Firebase ID Tokens passed in the Authorization HTTP header.
// The Firebase ID token needs to be passed as a Bearer token in the Authorization HTTP header like this:
// `Authorization: Bearer <Firebase ID Token>`.
// when decoded successfully, the ID Token content will be added as `req.user`.
const validateFirebaseIdToken = async (req, res, next) => {
  console.log('Check if request is authorized with Firebase ID token');

  if ((!req.headers.authorization || !req.headers.authorization.startsWith('Bearer ')) &&
      !(req.cookies && req.cookies.__session)) {
    console.error('No Firebase ID token was passed as a Bearer token in the Authorization header.',
        'Make sure you authorize your request by providing the following HTTP header:',
        'Authorization: Bearer <Firebase ID Token>',
        'or by passing a "__session" cookie.');
    res.status(401).send(parseError("Unauthorized", "TokenError", "nema tokena bre"));
    return;
  }

  let idToken;
  if (req.headers.authorization && req.headers.authorization.startsWith('Bearer ')) {
    console.log('Found "Authorization" header');
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
    console.log('ID Token correctly decoded', decodedIdToken);
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
//app.use(validateFirebaseIdToken);
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
   //res.send(`Hellow ${req.user.name}`);
});

app.get('/user/get/:userId', (req, res) => {
  var userRef = db.collection('users').doc(req.params.userId)
  userRef.get().then(function(doc) {
    if (doc.exists) {
      var friendsRef = []
      var user = doc.data()
      user.friends.forEach(function(id) {
        friendsRef.push(db.collection('users').doc(id))
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
          //user.friends = JSON.stringify(allFriends)
          console.log(user)
          res.status(200).json(user);
        })     
      } else {
        user.friends = allFriends
        res.status(200).json(user);
      }  
    } else {
      // var newUser = {
      //   id: req.params.userId,
      //   avatar: null,
      //   name: null,
      //   friends: []
      // };
      // userRef.set(newUser)
      // console.log('Added user document with ID: ', req.body.id);
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
  var userRef = db.collection('users').doc(req.params.userId)
  userRef.update({
    "friends": admin.firestore.FieldValue.arrayRemove(req.params.friendId)
  }).catch(error => {
    res.status(500).send(parseError("Failed to delete friend" + req.params.friendId));
  })
  var friendRef = db.collection('users').doc(req.params.friendId)
  friendRef.update({
    "friends": admin.firestore.FieldValue.arrayRemove(req.params.userId)
  }).catch(error => {
    res.status(500).send(parseError("Failed to delete friend of other" + req.params.userId));
  })
  res.status(200).json({});
});

app.post('/user/save', (req, res) => {
  var userRef = db.collection('users').doc(req.body.id)
  userRef.get().then(function(thisDoc) {
    if (thisDoc.exists) {
      var updatedUser = {
        avatar: req.body.avatar,
        name: req.body.name
      };
      userRef.update(updatedUser)
      console.log('Updated user document with ID: ', req.body.id);
    } else {
      var newUser = {
        id: req.body.id,
        avatar: req.body.avatar,
        name: req.body.name,
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
  var friendRef = db.collection('users').doc(req.body.friendId)
  friendRef.get().then(function(thisDoc) {
    if (thisDoc.exists) {
      friendRef.update({
        friends: admin.firestore.FieldValue.arrayUnion(req.params.userId)
      })
      console.log('Updated user document with ID: ', req.body.friendId);

      var userRef = db.collection('users').doc(req.params.userId)
      userRef.update({
        friends: admin.firestore.FieldValue.arrayUnion(req.body.friendId)
      }).then(function() {
        console.log('Successfully added friend with ID: ', req.params.friendId);
        res.status(201).send(thisDoc.data());
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
      
      res.status(404).send(parseError("User does not exists in system", ErrorType.USER_DOES_NOT_EXISTS));
    }
  }).catch(error => {
    console.log(error)
    res.status(500).send(parseError("Failed to add friend into Firestore"));
  });
  
});


app.post('/user/friends', (req, res) => {
  console.log(req.body.friendsId);
  try {
    var friendsRef = [];
    var friendsIds = req.body.friendsId;
    friendsIds.forEach(function (friendId) {
        friendsRef.push(db.collection('users').doc(friendId));
    });
    getAllFriends(res, friendsRef)
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

// async function getFriends(req: any, res: any, document: any, friends: any, isLastItem: boolean) {
//   document.ref.collection("product").get().then((querySnapshot) => {
//      querySnapshot.forEach((document) => {
//       friends.push(document.data())
//      });
//      if (isLastItem) {
//        console.log(friends)
//        res.status(200).json(friends);
//      }
//    });
//  }

app.post('/shopping-list/add-editor', (req, res) => {
  var shoppingListRef = db.collection('shopping_lists').doc(req.body.shoppingListId)
  shoppingListRef.get().then(function(shoppingList) {
    if (shoppingList.exists) {
      shoppingListRef.update({
        editors: admin.firestore.FieldValue.arrayUnion(req.body.editorId)
      })
      console.log('Added editor with ID: ', req.body.editorId);
      res.status(201).json({});
    } else {
      res.status(404).send(parseError("ShoppingList does not exists in database with ID: " + req.body.shoppingListId));
    }
  }).catch(error => {
    console.log(error)
    res.status(500).send(parseError("Failed to add editor into Firestore"));
  });
});

app.put('/shopping-list/delete-editor', (req, res) => {
  var shoppingListRef = db.collection('shopping_lists').doc(req.body.shoppingListId)
  shoppingListRef.get().then(function(shoppingList) {
    if (shoppingList.exists) {
      shoppingListRef.update({
        editors: admin.firestore.FieldValue.arrayRemove(req.body.editorId)
      })
      console.log('Deleted editor with ID: ', req.body.editorId);
      res.status(201).json({});
    } else {
      res.status(404).send(parseError("ShoppingList does not exists in database with ID: " + req.body.shoppingListId));
    }
  }).catch(error => {
    console.log(error)
    res.status(500).send(parseError("Failed to delete editor from Firestore"));
  });
});

app.post('/shopping-list/add', (req, res) => {
  var shoppingListRef = db.collection('shopping_lists').doc(req.body.id)
  shoppingListRef.set(req.body).then(function() {
    res.status(201).json({});
  }).catch(error => {
    res.status(500).send(parseError("Failed to add shopping list into Firestore with ID: " + req.body.id));
  });
});

app.put('/shopping-list/update', (req, res) => {
  var shoppingListRef = db.collection('shopping_lists').doc(req.body.id)
  shoppingListRef.update(req.body).then(function() {
    res.status(201).json({});
  }).catch(error => {
    res.status(500).send(parseError("Failed to update shopping list into Firestore with ID: " + req.body.id));
  });
});


app.delete('/shopping-list/delete/:shoppingListId', (req, res) => {
  try {
    const shoppingListProductsCollection = db.collection('shopping_lists').doc(req.params.shoppingListId).collection('product')
    var deleted = new Promise((resolve, reject) => {
      deleteQueryBatch(shoppingListProductsCollection, 10, resolve, reject)
    }).then(function() {
      db.collection('shopping_lists').doc(req.params.shoppingListId).delete();
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
    db.collection('shopping_lists').doc(req.params.shoppingListId).collection('product').doc(req.params.productId).delete();
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
  var productRef = db.collection('shopping_lists').doc(req.body.shoppingListId).collection('product').doc(req.body.id)
  productRef.update(req.body).then(function() {
    res.status(201).json({});
  }).catch(error => {
    res.status(500).send(parseError("Failed to update product into Firestore with ID: " + req.body.id));
  });
});

app.post('/product/add', (req, res) => {
  var productRef = db.collection('shopping_lists').doc(req.body.shoppingListId).collection('product').doc(req.body.id)
  productRef.set(req.body).then(function() {
    res.status(201).json({});
  }).catch(error => {
    res.status(500).send(parseError("Failed to add product into Firestore with ID: " + req.body.id));
  });
});

app.post('/products', (req, res) => {
  console.log(req.body.shoppingListsId);
  try {
    var products = [];
    var shoppingListids = req.body.shoppingListsId
    shoppingListids.forEach(function (shoppingListId, i) {
      db.collection('shopping_lists').doc(shoppingListId).get().then(document => {
        if (document.empty) {
          console.log("No matching documents for shoppings lists to fetch products");
          res.status(204).json({});
          return
        }
        if (i == shoppingListids.length - 1) {
          getProducts(req, res, document, products, true)
        } else {
          getProducts(req, res, document, products, false)
        }
      });
    }); 
  }catch(error) {
    res.status(500).send(parseError("Failed to fetch"));
  };
});

 async function getShopingingList(req: any, res: any, products: any, shoppingListId: any, isLastItem: boolean) {
  db.collection('shopping_lists').doc(shoppingListId).get().then(document => {
    if (document.empty) {
      console.log("No matching documents for shoppings lists to fetch products");
      res.status(204).json({});
      return
    }
    getProducts(req, res, document, products, isLastItem)
  });
}

async function getProducts(req: any, res: any, document: any, products: any, isLastItem: boolean) {
 document.ref.collection("product").get().then((querySnapshot) => {
    querySnapshot.forEach((document) => {
      products.push(document.data())
    });
    if (isLastItem) {
      console.log(products)
      res.status(200).json(products);
    }
  });
}

app.get('/shopping-list/:userId', (req, res) => {
  console.log(req.params.userId);
  try {
    const shoppingListsRef = db.collection('shopping_lists');
    shoppingListsRef.where('ownerId', '==', req.params.userId).get().then(snapshot => {
      if (snapshot.empty) {
        console.log("No matching documents for shoppings lists");
        res.status(204).json({});
        return
      }
      var shoppingLists = getShoppingLists(req, snapshot, res)
    });
  }catch(error) {
    res.status(500).send(parseError("Failed to get shopping lists Firestore"));
  };
});

async function getShoppingLists(req: any, snapshot: any, res: any) {
    var shoppingLists = [];
    for (let snap of snapshot.docs) {
      var shopingList = snap.data();
      var editorsRef = [];
      shopingList.editors.forEach(function(id) {
        editorsRef.push(db.collection('users').doc(id));
      });
      await getShoppingListsWithEditors(editorsRef, shopingList, shoppingLists)
    };
    console.log(shoppingLists)
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

  batch.set(db.collection('users').doc('qqqqqqqqq111111111'), user1);
  batch.set(db.collection('users').doc('wwwwwwwwwwww22222222'), user2);
  batch.set(db.collection('users').doc('eeeeeeeeeee3333333333'), user3);
  batch.set(db.collection('shopping_lists').doc('111'), shoppingList1);
  batch.set(db.collection('shopping_lists').doc('222'), shoppingList2);
  batch.set(db.collection('shopping_lists').doc('111').collection('products').doc('111111111'), product1);
  batch.set(db.collection('shopping_lists').doc('111').collection('products').doc('222222222'), product2);
  batch.set(db.collection('shopping_lists').doc('222').collection('products').doc('333333333'), product3);

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
