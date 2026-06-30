import Link from "next/link";
import { getWorlds } from "@/actions/worldActions";
import { Card, CardContent, CardDescription, CardHeader, CardTitle } from "@/components/ui/card";
import { Button } from "@/components/ui/button";

export const dynamic = "force-dynamic";

export default async function WorldsPage() {
  const worlds = await getWorlds();

  return (
    <div className="space-y-10">
      <div className="flex items-center justify-between">
        <div>
          <h1 className="text-3xl font-bold tracking-tight">
            Worlds
          </h1>
          <p className="text-muted-foreground text-sm mt-1">
            Manage your universes, stories, and characters
          </p>
        </div>
        <Button asChild>
          <Link href="/worlds/add">Create world</Link>
        </Button>
      </div>
      {worlds.length === 0 ? (
        <Card className="border-dashed">
          <CardHeader>
            <CardTitle>No worlds yet</CardTitle>
            <CardDescription>
              Start by creating your first universe
            </CardDescription>
          </CardHeader>
          <CardContent>
            <Button asChild variant="outline">
              <Link href="/worlds/add">Create your first world</Link>
            </Button>
          </CardContent>
        </Card>
      ) : (
        <div className="grid gap-5 md:grid-cols-2">
          {worlds.map((world: any) => (
            <Link key={world.id} href={`/worlds/${world.id}`}>
              <Card className="hover:shadow-md transition cursor-pointer">
                <CardHeader>
                  <CardTitle>{world.title}</CardTitle>
                  <CardDescription className="line-clamp-2">
                    {world.description || "No description yet"}
                  </CardDescription>
                </CardHeader>
              </Card>
            </Link>
          ))}
        </div>
      )}
    </div>
  );
}