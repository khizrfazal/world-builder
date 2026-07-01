import { BackLink } from "@/components/ui/back-link";
import { Character } from "@/types/Character";
import { wbClient } from "@/utils/client";
import Link from "next/link";
import {
  Card,
  CardHeader,
  CardTitle,
  CardDescription,
} from "@/components/ui/card";
import { Button } from "@/components/ui/button";

export const dynamic = "force-dynamic";

export default async function CharactersPage({ params }: any) {
  const { worldId } = await params;

  const characters: Character[] = await wbClient.get(
    `/worlds/${worldId}/characters`
  );

  return (
    <div className="space-y-12">
      <BackLink />

      {/* HEADER */}
      <header className="flex flex-col gap-6 sm:flex-row sm:items-start sm:justify-between">
        <div className="space-y-3">
          <h1 className="text-4xl font-bold tracking-tight">
            Characters
          </h1>

          <p className="max-w-2xl leading-relaxed text-muted-foreground">
            Create and manage characters in your world
          </p>
        </div>

        <Button
          asChild
          variant="outline"
          className="px-6 py-3 text-sm font-semibold"
        >
          <Link href={`/worlds/${worldId}/characters/add`}>
            Add Character
          </Link>
        </Button>
      </header>

      {/* CHARACTERS LIST */}
      <section className="space-y-4">
        <h2 className="text-sm font-medium uppercase tracking-widest text-muted-foreground">
          Characters Overview
        </h2>

        <div className="grid gap-4 sm:grid-cols-2 lg:grid-cols-3">
          {characters.length === 0 && (
            <p className="text-muted-foreground">No characters yet.</p>
          )}

          {characters.map((c) => (
            <Link
              key={c.id}
              href={`/worlds/${worldId}/characters/${c.id}`}
            >
              <Card className="group h-full cursor-pointer transition-all hover:-translate-y-0.5 hover:border-foreground/20 hover:shadow-md">
                <CardHeader>
                  <CardTitle className="text-lg">{c.name}</CardTitle>
                  <CardDescription>
                    {c.summary || "No summary provided."}
                  </CardDescription>
                </CardHeader>
              </Card>
            </Link>
          ))}
        </div>
      </section>
    </div>
  );
}