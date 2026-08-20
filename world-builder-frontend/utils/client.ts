import { serverClient } from "./server-client";
import { browserClient } from "./browser-client";

export const wbClient = typeof window === "undefined" ? serverClient : browserClient;
