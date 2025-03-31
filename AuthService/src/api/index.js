import express from "express";
import Authentication from "./Authentication.js"
import Authorization from "./Authorization.js"
import Management from "./Management.js"


const router = express.Router();

router.use("/api/auth", Authentication)
router.use("/api/auth", Authorization)
router.use("/api", Management)

export default router