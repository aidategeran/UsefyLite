
import './App.css'

import { useEffect } from "react";

function App() {
    useEffect(() => {
        fetch("/api/auth/test")
            .then((res) => res.text())
            .then((data) => console.log("Backend says:", data))
            .catch((err) => console.error(err));
    }, []);

    return (
        <div>
            <h1>Frontend is running ✅</h1>
            <p>Check the browser console to see if backend responds.</p>
        </div>
    );
}

export default App;
