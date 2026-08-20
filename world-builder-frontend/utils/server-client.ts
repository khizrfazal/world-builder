class ServerClient {
  constructor(private baseUrl: string) {}

  private getToken(token: string | null) {
    return token;
  }

  private async send<T>(
    path: string,
    method: string,
    body?: unknown,
    token?: string | null
  ): Promise<T> {
    const authToken = this.getToken(token);

    const res = await fetch(`${this.baseUrl}${path}`, {
      method,
      headers: {
        "Content-Type": "application/json",
        ...(authToken ? { Authorization: `Bearer ${authToken}` } : {}),
      },
      cache: "no-store",
      body: body ? JSON.stringify(body) : undefined,
    });

    if (!res.ok) {
      const text = await res.text().catch(() => "");
      throw new Error(`API error: ${res.status}${text ? ` - ${text}` : ""}`);
    }

    if (res.status === 204) return undefined as T;

    return await res.json();
  }

  get<T>(path: string, token?: string | null) {
    return this.send<T>(path, "GET", undefined, token);
  }

  post<T>(path: string, body?: unknown, token?: string | null) {
    return this.send<T>(path, "POST", body, token);
  }

  put<T>(path: string, body?: unknown, token?: string | null) {
    return this.send<T>(path, "PUT", body, token);
  }

  delete<T>(path: string, token?: string | null) {
    return this.send<T>(path, "DELETE", undefined, token);
  }
}

export const serverClient = new ServerClient(process.env.NEXT_PUBLIC_API_URL!);