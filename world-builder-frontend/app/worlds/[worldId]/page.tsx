import { getWorld } from "@/actions/worldActions";
import { getCharacters } from "@/actions/characterActions";
import CharacterSection from "@/components/CharacterSection";

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
  AlertDialogAction,
  AlertDialogCancel,
  AlertDialogContent,
  AlertDialogDescription,
  AlertDialogFooter,
  AlertDialogHeader,
  AlertDialogTitle,
  AlertDialogTrigger,
} from "@/components/ui/alert-dialog";

import { deleteWorld } from "@/actions/worldActions";
import { redirect } from "next/navigation";

export const dynamic = "force-dynamic";

export default async function WorldPage({ params }: any) {
  const { worldId } = await params;

  const world = await getWorld(worldId);
  const characters = await getCharacters(worldId);

  async function handleDelete() {
    "use server";
    await deleteWorld(worldId);
    redirect("/worlds");
  }

  const systems = [
    "Characters",
    "Locations",
    "Factions",
    "Cultures",
    "Events",
    "Items",
    "Creatures",
    "Magic Systems",
    "Lore Entries",
  ];

  return (
    <div className="space-y-10">
      <div className="flex items-start justify-between gap-6">
        <div className="space-y-2">
          <h1 className="text-4xl font-bold tracking-tight">
            {world.title}
          </h1>
          <p className="text-zinc-500 max-w-2xl leading-relaxed">
            {world.description || "No description yet — shape your world."}
          </p>
        </div>
        <div className="flex gap-2">
          <Link href={`/worlds/edit?worldId=${worldId}`}>
            <Button variant="outline">Edit</Button>
          </Link>
          <AlertDialog>
            <AlertDialogTrigger asChild>
              <Button variant="destructive">Delete</Button>
            </AlertDialogTrigger>
            <AlertDialogContent>
              <AlertDialogHeader>
                <AlertDialogTitle>
                  Delete this world?
                </AlertDialogTitle>
                <AlertDialogDescription>
                  This action is permanent. All characters, locations, and lore
                  will be removed.
                </AlertDialogDescription>
              </AlertDialogHeader>
              <AlertDialogFooter>
                <AlertDialogCancel>Cancel</AlertDialogCancel>
                <form action={handleDelete}>
                  <AlertDialogAction asChild>
                    <button
                      type="submit"
                      className="bg-red-600 text-white hover:bg-red-700 px-4 py-2 rounded-md"
                    >
                      Delete
                    </button>
                  </AlertDialogAction>
                </form>
              </AlertDialogFooter>
            </AlertDialogContent>
          </AlertDialog>
        </div>
      </div>
      <div className="rounded-2xl border bg-white/70 backdrop-blur shadow-sm p-6">
        <CharacterSection
          worldId={worldId}
          characters={characters}
        />
      </div>
      <div className="grid gap-5 md:grid-cols-2 lg:grid-cols-3">
        {systems.map((section) => {
          const slug = section.toLowerCase().replace(/ /g, "-");

          return (
            <Link
              key={section}
              href={`/worlds/${worldId}/${slug}`}
              className="block"
            >
              <Card className="h-full cursor-pointer transition hover:-translate-y-0.5 hover:shadow-lg bg-white/70 backdrop-blur border-zinc-200">
                <CardHeader>
                  <CardTitle className="text-lg">
                    {section}
                  </CardTitle>
                  <CardDescription>
                    Manage {section.toLowerCase()} in this world
                  </CardDescription>
                </CardHeader>
                <CardContent>
                  <p className="text-sm text-zinc-500">
                    Open →
                  </p>
                </CardContent>
              </Card>
            </Link>
          );
        })}
      </div>
    </div>
  );
}