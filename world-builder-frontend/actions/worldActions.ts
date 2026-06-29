"use server";

import { wbClient } from "@/utils/client";

export async function getWorlds() {
  return wbClient.get("/worlds");
}

export async function createWorld(data: { title: string, description?: string }) {
  return wbClient.post("/worlds", data);
}