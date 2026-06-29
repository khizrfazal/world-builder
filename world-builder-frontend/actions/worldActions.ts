"use server";

import { wbClient } from "@/utils/client";
import { World } from "@/types/World";

export async function getWorlds(): Promise<World[]> {
  return wbClient.get<World[]>("/worlds");
}

export async function getWorld(id: string): Promise<World> {
  return wbClient.get<World>(`/worlds/${id}`);
}

export async function deleteWorld(worldId: string) {
  return wbClient.delete(`/worlds/${worldId}`);
}

export async function createWorld(data: {
  title: string;
  description?: string;
}): Promise<World> {
  return wbClient.post<World>("/worlds", data);
}