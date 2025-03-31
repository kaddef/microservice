import express from "express";
import userService from "../services/userService.js";

const router = express.Router();

router.get("/users/:id", async (req, res) => {
    const result = await userService.getUserFromId(req.params.id)
    if(!result.success) {
        return res.status(200).json({data: null, error: "User not found" });
    }
    const {id, password, createdAt, updatedAt, ...rest} = result.userData;
    return res.status(200).json({data: rest, error: null})
})

export default router