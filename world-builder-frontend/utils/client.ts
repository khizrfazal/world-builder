class Client {
  private readonly baseUrl: string;

  constructor(baseUrl: string) {
    this.baseUrl = baseUrl;
  }

  async get<T>(path: string): Promise<T> {
    return this.send<T>(path, "GET");
  }

  async post<T>(path: string, body?: unknown): Promise<T> {
    return this.send<T>(path, "POST", body);
  }

  async put<T>(path: string, body?: unknown): Promise<T> {
    return this.send<T>(path, "PUT", body);
  }

  async delete<T>(path: string): Promise<T> {
    return this.send<T>(path, "DELETE");
  }

  private async send<T>(
    path: string,
    method: string,
    body?: unknown
  ): Promise<T> {
    const res = await fetch(`${this.baseUrl}${path}`, {
      method,
      headers: {
        "Content-Type": "application/json",
      },
      cache: "no-store",
      body: body ? JSON.stringify(body) : undefined,
    });

    // ❌ handle HTTP errors properly
    if (!res.ok) {
      const errorText = await res.text().catch(() => "");
      throw new Error(
        `API error: ${res.status}${errorText ? ` - ${errorText}` : ""}`
      );
    }

    // ✅ handle empty responses (DELETE / 204 No Content)
    if (res.status === 204) {
      return undefined as T;
    }

    // ✅ safely read response
    const text = await res.text();

    if (!text) {
      return undefined as T;
    }

    try {
      return JSON.parse(text) as T;
    } catch {
      throw new Error("Invalid JSON returned from API");
    }
  }
}

export const wbClient = new Client(process.env.NEXT_PUBLIC_API_URL!);