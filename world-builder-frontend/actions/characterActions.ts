"use server";

import { wbClient } from "@/utils/client";
import { Character } from "@/types/Character";

export async function getCharacters(worldId: string): Promise<Character[]> {
  return wbClient.get<Character[]>(`/worlds/${worldId}/characters`);
}

export async function createCharacter(
  worldId: string,
  data: { name: string; summary?: string }
): Promise<string> {
  return wbClient.post<string>(`/worlds/${worldId}/characters`, data);
}