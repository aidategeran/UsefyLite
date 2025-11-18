
import React from "react";
import ReactDOM from "react-dom/client";
import { BrowserRouter, Routes, Route } from "react-router-dom";
import App from "./App";
import Index from "./pages/Index";
import Register from "./pages/Register";
import "./index.css";
import Success from "./pages/Success.tsx";

ReactDOM.createRoot(document.getElementById("root")!).render(
    <React.StrictMode>
        <BrowserRouter>
            <Routes>
                <Route path="/" element={<App />}>
                    <Route index element={<Index />} />
                    <Route path="register" element={<Register />} />
                    <Route path="success" element={<Success />} />
                </Route>
            </Routes>
        </BrowserRouter>
    </React.StrictMode>
);