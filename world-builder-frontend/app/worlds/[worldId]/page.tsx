import { getWorld, deleteWorld } from "@/actions/worldActions";
import { getCharacters } from "@/actions/characterActions";
import { BackLink } from "@/components/ui/back-link";
import {
  Card,
  CardContent,
  CardDescription,
  CardHeader,
  CardTitle,
} from "@/components/ui/card";
import { Button } from "@/components/ui/button";
import Link from "next/link";
import {
  AlertDialog,
  AlertDialogCancel,
  AlertDialogContent,
  AlertDialogDescription,
  AlertDialogFooter,
  AlertDialogHeader,
  AlertDialogTitle,
  AlertDialogTrigger,
} from "@/components/ui/alert-dialog";
import { redirect } from "next/navigation";
import { wbClient } from "@/utils/client";

export const dynamic = "force-dynamic";

export default async function WorldPage({ params }: any) {
  const { worldId } = await params;
  const world = await wbClient.get(`/worlds/${worldId}`);
  const characters = await wbClient.get(`/worlds/${worldId}/characters`);

  const handleDelete = async () => {
    "use server";
    await deleteWorld(worldId);
    redirect("/worlds");
  };

  // 🟦 WORLD CONTENT (formerly nodes)
  const contentSections = [
    { name: "Characters", slug: "characters", description: "Create and manage characters in your world." },
    { name: "Locations", slug: "locations", description: "Define places, regions, and landmarks." },
    { name: "Factions", slug: "factions", description: "Organisations, kingdoms, guilds, and groups." },
    { name: "Events", slug: "events", description: "Battles, discoveries, meetings, prophecies." },
    { name: "Lore Entries", slug: "lore-entries", description: "Write lore, history, myths, and world notes." },
  ];

  // 🟧 WORLD RELATIONSHIPS (formerly edges)
  const relationshipSections = [
    { name: "Character Relationships", slug: "character-relationships", description: "Allies, rivals, family, enemies." },
    { name: "Character Locations", slug: "character-locations", description: "Track where characters are located." },
    { name: "Faction Locations", slug: "faction-locations", description: "See which factions control or influence places." },
    { name: "Event Participants", slug: "event-characters", description: "Add characters involved in events." },
  ];

  return (
    <div className="space-y-12">
      {/* HEADER */}
      <BackLink/>
      <header className="flex flex-col gap-6 sm:flex-row sm:items-start sm:justify-between">
        <div className="space-y-3">
          <p className="text-xs font-medium uppercase tracking-widest text-muted-foreground">
            World Overview
          </p>

          <h1 className="text-4xl font-bold tracking-tight text-balance">
            {world.title}
          </h1>

          <p className="max-w-2xl leading-relaxed text-muted-foreground text-pretty">
            {world.description || "No description yet — shape your world."}
          </p>
        </div>

        {/* ACTIONS */}
        <div className="flex shrink-0 gap-3">
          <Button
            asChild
            variant="outline"
            className="px-6 py-3 text-sm font-semibold"
          >
            <Link href={`/worlds/${worldId}/edit`}>Edit world</Link>
          </Button>
          <AlertDialog>
            <AlertDialogTrigger asChild>
              <Button className="px-6 py-3 text-sm font-semibold bg-red-600 text-white hover:bg-red-700">
                Delete world
              </Button>
            </AlertDialogTrigger>

            <AlertDialogContent>
              <AlertDialogHeader>
                <AlertDialogTitle>Delete this world?</AlertDialogTitle>
                <AlertDialogDescription>
                  This action is permanent. All characters, locations, factions,
                  events, and lore will be removed.
                </AlertDialogDescription>
              </AlertDialogHeader>

              <AlertDialogFooter>
                <AlertDialogCancel>Cancel</AlertDialogCancel>

                <form action={handleDelete}>
                  <Button
                    type="submit"
                    className="bg-red-600 text-white hover:bg-red-700"
                  >
                    Delete world
                  </Button>
                </form>
              </AlertDialogFooter>
            </AlertDialogContent>
          </AlertDialog>
        </div>
      </header>

      {/* 🟦 WORLD CONTENT */}
      <section className="space-y-4">
        <h2 className="text-sm font-medium uppercase tracking-widest text-muted-foreground">
          World Content
        </h2>

        <div className="grid gap-4 sm:grid-cols-2 lg:grid-cols-3">
          {contentSections.map((section) => (
            <Link key={section.slug} href={`/worlds/${worldId}/${section.slug}`}>
              <Card className="group h-full cursor-pointer transition-all hover:-translate-y-0.5 hover:border-foreground/20 hover:shadow-md">
                <CardHeader>
                  <CardTitle className="text-lg">{section.name}</CardTitle>
                  <CardDescription>{section.description}</CardDescription>
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
      </section>

      {/* 🟧 WORLD RELATIONSHIPS */}
      <section className="space-y-4">
        <h2 className="text-sm font-medium uppercase tracking-widest text-muted-foreground">
          World Relationships
        </h2>

        <div className="grid gap-4 sm:grid-cols-2 lg:grid-cols-3">
          {relationshipSections.map((section) => (
            <Link key={section.slug} href={`/worlds/${worldId}/${section.slug}`}>
              <Card className="group h-full cursor-pointer transition-all hover:-translate-y-0.5 hover:border-foreground/20 hover:shadow-md">
                <CardHeader>
                  <CardTitle className="text-lg">{section.name}</CardTitle>
                  <CardDescription>{section.description}</CardDescription>
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
      </section>
    </div>
  );
}