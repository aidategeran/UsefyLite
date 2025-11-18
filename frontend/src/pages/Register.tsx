
import React, { useState } from "react";
import { useNavigate } from "react-router-dom";

export default function Register() {
    const [form, setForm] = useState({ username: "", password: "" });
    const [errors, setErrors] = useState<{ username?: string; password?: string }>({});
    const [message, setMessage] = useState("");
    const navigate = useNavigate();

    const handleChange = (e: React.ChangeEvent<HTMLInputElement>) => {
        const { name, value } = e.target;
        setForm({ ...form, [name]: value });
        setErrors({ ...errors, [name]: "" });
    };

    const handleSubmit = async (e: React.FormEvent) => {
        e.preventDefault();

        try {
            const response = await fetch("http://localhost:8080/register", {
                method: "POST",
                headers: {
                    "Content-Type": "application/json",
                },
                body: JSON.stringify(form),
            });

            if (response.status === 400) {
                const data = await response.json();
                setErrors(data);
                return;
            }

            if (response.ok) {
                setMessage("Registration successful!");

                // Redirect after 1 second
                setTimeout(() => {
                    navigate("/success");
                }, 1000);
            }

        } catch (error) {
            setMessage("Something went wrong.");
        }
    };

    return (
        <form onSubmit={handleSubmit}>
            <h2>Register</h2>

            <input
                name="username"
                value={form.username}
                onChange={handleChange}
                placeholder="Username"
            />
            {errors.username && <p style={{ color: "red" }}>{errors.username}</p>}

            <input
                type="password"
                name="password"
                value={form.password}
                onChange={handleChange}
                placeholder="Password"
            />
            {errors.password && <p style={{ color: "red" }}>{errors.password}</p>}

            <button type="submit">Register</button>

            {message && <p style={{ color: "green" }}>{message}</p>}
        </form>
    );
}
