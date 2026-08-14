import { jwtDecode } from "jwt-decode";

export const isTokenValid = () => {
    const token = localStorage.getItem("accessToken");

    if (!token) {
        return false;
    }

    try {
        const decoded = jwtDecode(token);

        return decoded.exp > Date.now() / 1000;
    } catch (error) {
        return false;
    }
};