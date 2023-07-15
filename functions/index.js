// /* eslint-disable */
/* eslint-disable max-len  */

const functions = require("firebase-functions");
const admin = require("firebase-admin");
admin.initializeApp();

exports.newUserCreated = functions.auth.user().onCreate((user) => {
  console.log("new user created phone", user.phone);
  console.log("new user created email", user.email);
  console.log("new user created uid", user.uid);
  console.log("new user created name", user.name);
  return admin.firestore().collection("users").doc(user.uid).set({
    name: "Name Not Found",
    phone: "Phone Not Found",
    email: "Email Not Found",
    uid: user.uid,
  });
});

