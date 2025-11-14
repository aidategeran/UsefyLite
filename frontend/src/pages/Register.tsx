
import React, { useState } from "react";
import "./Register.css";

export default function Register() {
    const [form, setForm] = useState({ username: "", password: "" });
    const [errors, setErrors] = useState<{ username?: string; password?: string }>({});
    const [message, setMessage] = useState("");

    const handleChange = (e: React.ChangeEvent<HTMLInputElement>) => {
        const { name, value } = e.target;
        setForm({ ...form, [name]: value });
        setErrors({ ...errors, [name]: "" });
    };

    const validate = () => {
        const newErrors: { username?: string; password?: string } = {};
        if (!form.username.trim()) newErrors.username = "Username is required";
        if (!form.password.trim()) newErrors.password = "Password is required";
        else if (form.password.length < 6) newErrors.password = "Password must be at least 6 characters";
        return newErrors;
    };

    const handleSubmit = async (e: React.FormEvent) => {
        e.preventDefault();
        const validationErrors = validate();

        if (Object.keys(validationErrors).length > 0) {
            setErrors(validationErrors);
            return;
        }

        try {
            const response = await fetch("http://localhost:8080/api/auth/register", {
                method: "POST",
                headers: { "Content-Type": "application/json" },
                body: JSON.stringify(form),
            });

            const text = await response.text();
            setMessage(response.ok ? text : `Error: ${text}`);
        } catch {
            setMessage("Error: could not connect to server");
        }
    };

    return (
        <form className="register-form" onSubmit={handleSubmit}>
            <h2>Register</h2>

            <label>
                Username:
                <input
                    name="username"
                    value={form.username}
                    onChange={handleChange}
                    placeholder="Enter username"
                />
                {errors.username && <p className="error">{errors.username}</p>}
            </label>

            <label>
                Password:
                <input
                    type="password"
                    name="password"
                    value={form.password}
                    onChange={handleChange}
                    placeholder="Enter password"
                />
                {errors.password && <p className="error">{errors.password}</p>}
            </label>

            <button type="submit">Sign Up</button>
            {message && <p className="message">{message}</p>}
        </form>
    );
}
