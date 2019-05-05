'use strict';
const functions = require('firebase-functions');
const admin = require('firebase-admin');
admin.initializeApp();
const express = require('express');
const bodyParser = require('body-parser')
const cookieParser = require('cookie-parser')();
const cors = require('cors')({origin: true});
const app = express();

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
  const testObject = {
    test: "Test"
  }
   console.error('Success', "Yey");
  res.json(testObject);
   //res.send(`Hellow ${req.user.name}`);
});

app.post('/user/add', (req, res) => {
  console.log(req.body);
  db.collection('users').doc(req.body.uid).set(req.body).then(ref => {
    console.log('Added user document with ID: ', ref.id);
    res.status(201).end();
  }).catch(error => {
    res.status(500).send(parseError("Failed to add user into Firestore"));
  });
});

app.post('/shopping-list/add', (req, res) => {
  console.log(req.body);
  db.collection('shopping_lists').doc(req.body.id).set(req.body).then(ref => {
    console.log('Added shoppingList document with ID: ', ref.id);
    res.status(201).end();
  }).catch(error => {
    res.status(500).send(parseError("Failed to add shopping list into Firestore"));
  });
});

app.get('/shopping-list/:userId', (req, res) => {
  console.log(req.params.userId);
  const shoppingListsRef = db.collection('shopping_lists');
  shoppingListsRef.where('ownerUid', '==', req.params.userId).get().then(snapshot => {
    if (snapshot.empty) {
      console.log("No matching documents for shoppings lists");
      res.status(200).end();
      return
    }
    var result = [];
    snapshot.forEach(doc => {
      result.push(doc.data)
    })
    res.status(200).send(result);
  }).catch(error => {
    res.status(500).send(parseError("Failed to add shopping list into Firestore"));
  });
});

app.get('/generate-sample-data', (req, res) => {
  var user1 = {
    uid: 'qqqqqqqqq111111111',
    name: 'Pera Peric',
    email: 'pera_peric@testmail.com',
    avatar: 'https://lh6.googleusercontent.com/-ZHu0DHyICb0/AAAAAAAAAAI/AAAAAAAAAGA/nTs_D1FNIzk/s96-c/photo.jpg',
    friends: ['wwwwwwwwwwww22222222', 'eeeeeeeeeee3333333333']
  }

  var user2 = {
    uid: 'wwwwwwwwwwww22222222',
    name: 'Ana Anic',
    email: 'ana_anic@testmail.com',
    avatar: 'https://lh6.googleusercontent.com/-ZHu0DHyICb0/wwwwwwww/222222222/nTs_D1FNIzk/s96-c/photo.jpg',
    friends: ['eeeeeeeeeee3333333333']
  }

  var user3 = {
    uid: 'eeeeeeeeeee3333333333',
    name: 'Milan Milance',
    email: 'milan_milance@testmail.com',
    avatar: 'https://lh6.googleusercontent.com/-ZHu0DHyICb0/eeeeeee/3333333/nTs_D1FNIzk/s96-c/photo.jpg',
    friends: []
  }

  var shoppingList1 = {
    id: '111',
    name: 'Party',
    ownerUid: 'qqqqqqqqq111111111',
    editors: ['wwwwwwwwwwww22222222', 'eeeeeeeeeee3333333333']
  }

  var shoppingList2 = {
    id: '222',
    name: 'Cake',
    ownerUid: 'qqqqqqqqq111111111',
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
