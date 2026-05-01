import axios from "axios";
import { login as loginApi } from "../api/auth";

import React, { useState } from "react";
import { useNavigate, useLocation } from "react-router-dom";

interface LocationState {
  from?: Location;
}

const Login: React.FC = () => {
  const [email, setEmail] = useState("");
  const [password, setPassword] = useState("");

  const navigate = useNavigate();

  // javascript arrow function closure
  const handleEmail = (event: React.ChangeEvent<HTMLInputElement>) => {
    setEmail(event.target.value);
  };

  const handlePassword = (event: React.ChangeEvent<HTMLInputElement>) => {
    setPassword(event.target.value);
  };

  const handleLogin = async () => {
    try {
      // http protocal
      // get, post, delete, patch
      // axios helps us handle this syncouesly
      const user = await loginApi(email, password);
      localStorage.setItem("user", JSON.stringify(user));
      navigate("/main");
    //   const res = await axios.post("http://localhost:8080/login", {
    //     email,
    //     password,
    //   });
    //   if (res.data.id) {
    //     localStorage.setItem("user", JSON.stringify(res.data));
    //     navigate("/main");
    //   }
    //   console.log(res.data);
    } catch (err) {
      alert(err);
    }
  };

  return (
    <div style={{ maxWidth: 400, margin: "0 auto", paddingTop: 50 }}>
      <h1>Login</h1>
      <div>
        Email:
        <input
          type="email"
          value={email}
          onChange={handleEmail}
          placeholder="Enter email"
        />
      </div>
      <div>
        Password:
        <input
          type="password"
          value={password}
          onChange={handlePassword}
          placeholder="Enter password"
        />
      </div>
      <button onClick={handleLogin}>Login</button>
    </div>
  );
};

export default Login;
