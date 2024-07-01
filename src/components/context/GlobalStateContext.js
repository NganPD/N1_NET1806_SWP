// src/contexts/GlobalStateContext.js
import React, { createContext, useState } from "react";

export const GlobalStateContext = createContext();

export const GlobalStateProvider = ({ children }) => {
  const [isRegisterSuccess, setIsRegisterSuccess] = useState(false);
  const [isLoginSuccess, setIsLoginSuccess] = useState(false);
  return (
    <GlobalStateContext.Provider
      value={{
        isRegisterSuccess,
        setIsRegisterSuccess,
        isLoginSuccess,
        setIsLoginSuccess,
      }}
    >
      {children}
    </GlobalStateContext.Provider>
  );
};
