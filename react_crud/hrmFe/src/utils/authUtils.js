import { jwtDecode } from "jwt-decode";

export const isTokenValid = () => {
    const expiresTime = localStorage.getItem("expiresTime")
    if (!expiresTime) {
        return false;
    }
    try {
        console.log("******"+expiresTime);
        console.log("******"+Date.now() / 1000);
        console.log("***CHECK***"+(expiresTime > Date.now() / 1000));

        return expiresTime > Date.now() / 1000;
    } catch (error) {
        return false;
    }
};