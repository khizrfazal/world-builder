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
      body: body ? JSON.stringify(body) : undefined,
    });

    if (!res.ok) {
      throw new Error(`API error: ${res.status}`);
    }

    return res.json();
  }
}

export const wbClient = new Client(process.env.API_URL!);