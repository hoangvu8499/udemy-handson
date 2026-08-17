// categoryService.js
import axios from "axios";
import { isTokenValid } from "./authUtils";

export const getAllCategories = () => {
        return axios.get(
            "http://localhost:8080/category/findall",
            {
                // headers: {
                //     Authorization: `Bearer ${localStorage.getItem("accessToken")}`
                // }
                timeout: 3000,
                withCredentials: true
            }
        );
    
};
