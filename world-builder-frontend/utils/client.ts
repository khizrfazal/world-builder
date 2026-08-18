import { cookies } from "next/headers";

class Client {
  private readonly baseUrl: string;

  constructor(baseUrl: string) {
    this.baseUrl = baseUrl;
  }

  private getToken(): string | null {
    try {
      return cookies().get("token")?.value ?? null;
    } catch {
      return null;
    }
  }

  private async send<T>(path: string, method: string, body?: unknown): Promise<T> {
    const token = this.getToken();

    const res = await fetch(`${this.baseUrl}${path}`, {
      method,
      headers: {
        "Content-Type": "application/json",
        ...(token ? { Authorization: `Bearer ${token}` } : {}),
      },
      cache: "no-store",
      body: body ? JSON.stringify(body) : undefined,
    });

    if (!res.ok) {
      const errorText = await res.text().catch(() => "");
      throw new Error(`API error: ${res.status}${errorText ? ` - ${errorText}` : ""}`);
    }

    if (res.status === 204) {
      return undefined as T;
    }

    const text = await res.text();
    if (!text) return undefined as T;

    try {
      return JSON.parse(text) as T;
    } catch {
      throw new Error("Invalid JSON returned from API");
    }
  }

  get<T>(path: string) { return this.send<T>(path, "GET"); }
  post<T>(path: string, body?: unknown) { return this.send<T>(path, "POST", body); }
  put<T>(path: string, body?: unknown) { return this.send<T>(path, "PUT", body); }
  delete<T>(path: string) { return this.send<T>(path, "DELETE"); }
}

export const wbClient = new Client(process.env.NEXT_PUBLIC_API_URL!);
