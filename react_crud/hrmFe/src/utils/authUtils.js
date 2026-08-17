import { jwtDecode } from "jwt-decode";

export const isTokenValid = () => {
    const expiresTime = localStorage.getItem("expiresTime")
    if (!expiresTime) {
        return false;
    }
    try {
        return expiresTime > Date.now() / 1000;
    } catch (error) {
        return false;
    }
};