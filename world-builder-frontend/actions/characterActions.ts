"use server";

import { wbClient } from "@/utils/client";
import { Character } from "@/types/Character";

export async function getCharacters(worldId: string): Promise<Character[]> {
  return wbClient.get<Character[]>(`/worlds/${worldId}/characters`);
}

export async function getCharacter(characterId: string): Promise<Character> {
  return wbClient.get<Character>(`/characters/${characterId}`);
}

export async function createCharacter(
  worldId: string,
  data: { name: string; summary?: string }
): Promise<string> {
  return wbClient.post<string>(`/worlds/${worldId}/characters`, data);
}

export async function updateCharacter(
  characterId: string,
  data: { name: string; summary?: string }
): Promise<void> {
  await wbClient.put(`/characters/${characterId}`, data);
}

export async function deleteCharacter(characterId: string): Promise<void> {
  await wbClient.delete(`/characters/${characterId}`);
}