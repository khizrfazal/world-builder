"use server";

import { cookies } from "next/headers";
import { wbClient } from "@/utils/client";

export async function login(username: string, password: string) {
  const res = await fetch(`${process.env.NEXT_PUBLIC_API_URL}/users/login`, {
    method: "POST",
    headers: { "Content-Type": "application/json" },
    body: JSON.stringify({ username, password }),
  });

  if (!res.ok) throw new Error("Login failed");

  const data = await res.json();

  // Store token in a cookie
  const cookieStore = await cookies();
  cookieStore.set("token", data.token, {
    httpOnly: true,
    secure: true,
    sameSite: "lax",
    path: "/",
  });

  return data;
}

export async function signup(username: string, password: string) {
  const res = await fetch(`${process.env.NEXT_PUBLIC_API_URL}/users/register`, {
    method: "POST",
    headers: { "Content-Type": "application/json" },
    body: JSON.stringify({ username, password }),
  });

  if (!res.ok) {
    const text = await res.text().catch(() => "");
    throw new Error(text || "Signup failed");
  }

  return await res.json().catch(() => ({}));
}

export async function logout() {
  const cookieStore = await cookies();
  cookieStore.delete("token");
}


export async function getCurrentUser() {
  try {
    return await wbClient.get<{ username: string }>("/users/me");
  } catch {
    return null;
  }
}