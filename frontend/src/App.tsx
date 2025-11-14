
import { Link, Outlet } from "react-router-dom";


export default function App() {
    return (
        <div>
            <nav style={{ display: "flex", gap: "1rem", padding: "1rem" }}>
                <Link to="/">Home</Link>
                <Link to="register">Register</Link>
            </nav>
            <Outlet />
        </div>
    );
}