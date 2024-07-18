// Import the functions you need from the SDKs you need
import { initializeApp } from "firebase/app";
import { GoogleAuthProvider } from "firebase/auth";
import { getAuth } from "firebase/auth";
import { getStorage } from "firebase/storage";

// TODO: Add SDKs for Firebase products that you want to use
// https://firebase.google.com/docs/web/setup#available-libraries

// Your web app's Firebase configuration
// For Firebase JS SDK v7.20.0 and later, measurementId is optional
const firebaseConfig = {
    apiKey: "AIzaSyA8eF9KTPbET-tq71IfdajrLsxtegGla_0",
    authDomain: "badminton-b9e99.firebaseapp.com",
    projectId: "badminton-b9e99",
    storageBucket: "badminton-b9e99.appspot.com",
    messagingSenderId: "106250518743",
    appId: "1:106250518743:web:7aba69d729ac184fdc7092",
    measurementId: "G-4BM1C8F28J"
};

// Initialize Firebase
export const app = initializeApp(firebaseConfig);
export const googleProvider = new GoogleAuthProvider();
export const auth = getAuth();
export const storage = getStorage(app);
