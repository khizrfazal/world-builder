class BrowserClient {
  constructor(private baseUrl: string) {}

  private getToken() {
    return localStorage.getItem("token");
  }

  private async send<T>(path: string, method: string, body?: unknown): Promise<T> {
    const token = this.getToken();

    const res = await fetch(`${this.baseUrl}${path}`, {
      method,
      headers: {
        "Content-Type": "application/json",
        ...(token ? { Authorization: `Bearer ${token}` } : {}),
      },
      body: body ? JSON.stringify(body) : undefined,
    });

    if (!res.ok) throw new Error(`API error: ${res.status}`);

    return await res.json();
  }

  get<T>(path: string) { return this.send<T>(path, "GET"); }
  post<T>(path: string, body?: unknown) { return this.send<T>(path, "POST", body); }
  put<T>(path: string, body?: unknown) { return this.send<T>(path, "PUT", body); }
  delete<T>(path: string) { return this.send<T>(path, "DELETE"); }
}

export const browserClient = new BrowserClient(process.env.NEXT_PUBLIC_API_URL!);