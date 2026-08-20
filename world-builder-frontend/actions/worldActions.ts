"use server";

import { cookies } from "next/headers";
import { serverClient } from "@/utils/server-client";
import { wbClient } from "@/utils/client";
import { World } from "@/types/World";

export async function getWorlds(): Promise<World[]> {
  const cookieStore = await cookies();
  const token = cookieStore.get("token")?.value ?? null;
  return serverClient.get<World[]>("/worlds", token);
}

export async function getWorld(id: string): Promise<World> {
  const cookieStore = await cookies();
  const token = cookieStore.get("token")?.value ?? null;
  return serverClient.get<World>(`/worlds/${id}`, token);
}

export async function deleteWorld(worldId: string) {
  const cookieStore = await cookies();
  const token = cookieStore.get("token")?.value ?? null;
  return serverClient.delete(`/worlds/${worldId}`, token);
}

export async function updateWorld(
  worldId: string,
  data: { title: string; description?: string }
): Promise<void> {
  const cookieStore = await cookies();
  const token = cookieStore.get("token")?.value ?? null;
  return serverClient.put(`/worlds/${worldId}`, data, token);
}

export async function createWorld(data: {
  title: string;
  description?: string;
}): Promise<World> {
  const cookieStore = await cookies();
  const token = cookieStore.get("token")?.value ?? null;
  return serverClient.post<World>("/worlds", data, token);
}