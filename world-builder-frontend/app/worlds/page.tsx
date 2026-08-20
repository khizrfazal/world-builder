import Link from "next/link";
import { cookies } from "next/headers";
import { serverClient } from "@/utils/server-client";
import { World } from "@/types/World";
import {
  Card,
  CardContent,
  CardDescription,
  CardHeader,
  CardTitle,
} from "@/components/ui/card";
import { Button } from "@/components/ui/button";
import { LogoutButton } from "@/components/ui/logout-button";

export const dynamic = "force-dynamic";

export default async function WorldsPage() {
  // Read token on the server
  const cookieStore = await cookies();
  const token = cookieStore.get("token")?.value ?? null;

  // Fetch worlds using serverClient
  const worlds: World[] = await serverClient.get("/worlds", token);
  const hasWorlds = worlds.length > 0;

  return (
    <div className="space-y-12">
      {/* HEADER */}
      <header className="flex items-center justify-between">
        <div className="space-y-2">
          <h1 className="text-3xl font-bold tracking-tight">Your Worlds</h1>
          <p className="text-muted-foreground text-sm">
            Create and manage the universes you’re building.
          </p>
        </div>

        <div className="flex gap-3">
          {hasWorlds && (
            <Button
              asChild
              className="px-6 py-3 font-semibold bg-black text-white hover:bg-black/90"
            >
              <Link href="/worlds/add">Create world</Link>
            </Button>
          )}
          <LogoutButton />
        </div>
      </header>

      {/* EMPTY STATE */}
      {!hasWorlds ? (
        <Card className="border-dashed">
          <CardHeader>
            <CardTitle>No worlds yet</CardTitle>
            <CardDescription>
              Start by creating your first universe.
            </CardDescription>
          </CardHeader>

          <CardContent>
            <Button
              asChild
              variant="outline"
              className="bg-black text-white hover:bg-black/90"
            >
              <Link href="/worlds/add">Create your first world</Link>
            </Button>
          </CardContent>
        </Card>
      ) : (
        <div className="grid gap-6 sm:grid-cols-2 lg:grid-cols-3">
          {worlds.map((world) => (
            <Link key={world.id} href={`/worlds/${world.id}`}>
              <Card className="group h-full cursor-pointer transition-all hover:-translate-y-0.5 hover:border-foreground/20 hover:shadow-md">
                <CardHeader>
                  <CardTitle className="text-lg">{world.title}</CardTitle>
                  <CardDescription className="line-clamp-2">
                    {world.description || "No description yet"}
                  </CardDescription>
                </CardHeader>

                <CardContent>
                  <p className="text-sm text-muted-foreground transition-colors group-hover:text-foreground">
                    Open →
                  </p>
                </CardContent>
              </Card>
            </Link>
          ))}
        </div>
      )}
    </div>
  );
}