import Link from "next/link";
import { cookies } from "next/headers";
import { serverClient } from "@/utils/server-client";
import { deleteWorld } from "@/actions/worldActions";
import { BackLink } from "@/components/ui/back-link";
import { World } from "@/types/World";
import { Character } from "@/types/Character";
import { Location } from "@/types/Location";
import { Faction } from "@/types/Faction";
import { Event } from "@/types/Event";
import { LoreEntry } from "@/types/LoreEntry";
import {
  Card,
  CardContent,
  CardDescription,
  CardHeader,
  CardTitle,
} from "@/components/ui/card";
import { Button } from "@/components/ui/button";
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

export const dynamic = "force-dynamic";

// Helper for singular/plural labels
function label(count: number, singular: string, plural: string) {
  return `${count} ${count === 1 ? singular : plural}`;
}

export default async function WorldPage({ params }: any) {
  const { worldId } = await params;

  const cookieStore = await cookies();
  const token = cookieStore.get("token")?.value ?? null;

  const world: World = await serverClient.get(`/worlds/${worldId}`, token);
  const characters: Character[] = await serverClient.get(`/worlds/${worldId}/characters`, token);
  const locations: Location[] = await serverClient.get(`/worlds/${worldId}/locations`, token);
  const factions: Faction[] = await serverClient.get(`/worlds/${worldId}/factions`, token);
  const events: Event[] = await serverClient.get(`/worlds/${worldId}/events`, token);
  const lore: LoreEntry[] = await serverClient.get(`/worlds/${worldId}/lore`, token);

  const handleDelete = async () => {
    "use server";
    await deleteWorld(worldId);
    redirect("/worlds");
  };

  const contentSections = [
    {
      name: "Characters",
      slug: "characters",
      description: "Create and manage characters in your world.",
      label: label(characters.length, "Character", "Characters"),
    },
    {
      name: "Locations",
      slug: "locations",
      description: "Define places, regions, and landmarks.",
      label: label(locations.length, "Location", "Locations"),
    },
    {
      name: "Factions",
      slug: "factions",
      description: "Organisations, kingdoms, guilds, and groups.",
      label: label(factions.length, "Faction", "Factions"),
    },
    {
      name: "Events",
      slug: "events",
      description: "Battles, discoveries, meetings, prophecies.",
      label: label(events.length, "Event", "Events"),
    },
    {
      name: "Lore Entries",
      slug: "lore-entries",
      description: "Write lore, history, myths, and world notes.",
      label: label(lore.length, "Lore Entry", "Lore Entries"),
    },
  ];

  const relationshipSections = [
    { name: "Character Relationships", slug: "character-relationships", description: "Allies, rivals, family, enemies." },
    { name: "Character Locations", slug: "character-locations", description: "Track where characters are located." },
    { name: "Faction Locations", slug: "faction-locations", description: "See which factions control or influence places." },
    { name: "Event Participants", slug: "event-characters", description: "Add characters involved in events." },
  ];

  return (
    <div className="space-y-12">
      <BackLink />

      {/* HEADER */}
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
          <Button asChild variant="outline" className="px-6 py-3 text-sm font-semibold">
            <Link href={`/worlds/${worldId}/edit`}>Edit world</Link>
          </Button>

          <AlertDialog>
            <AlertDialogTrigger asChild>
              <Button className="px-6 py-3 text-sm font-semibold bg-red-600 text-white hover:bg-red-600 cursor-pointer">
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
                  <Button type="submit" className="bg-red-600 text-white hover:bg-red-600 cursor-pointer">
                    Delete
                  </Button>
                </form>
              </AlertDialogFooter>
            </AlertDialogContent>
          </AlertDialog>
        </div>
      </header>

      {/* WORLD CONTENT */}
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

                <CardContent className="flex items-center justify-between">
                  <p className="text-sm text-muted-foreground">{section.label}</p>
                  <p className="text-sm text-muted-foreground group-hover:text-foreground">
                    Open →
                  </p>
                </CardContent>
              </Card>
            </Link>
          ))}
        </div>
      </section>

      {/* WORLD RELATIONSHIPS */}
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

                <CardContent className="flex items-center justify-end">
                  <p className="text-sm text-muted-foreground group-hover:text-foreground">
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