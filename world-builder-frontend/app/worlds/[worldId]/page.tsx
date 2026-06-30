import { getWorld, deleteWorld } from "@/actions/worldActions";
import { getCharacters } from "@/actions/characterActions";

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

export const dynamic = "force-dynamic";

export default async function WorldPage({ params }: any) {
  const { worldId } = params;

  const world = await getWorld(worldId);
  const characters = await getCharacters(worldId); // still useful later for counts

  const handleDelete = async () => {
    "use server";
    await deleteWorld(worldId);
    redirect("/worlds");
  };

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
    <div className="space-y-12">
      {/* HEADER */}
      <header className="flex flex-col gap-6 sm:flex-row sm:items-start sm:justify-between">
        <div className="space-y-3">
          <p className="text-xs font-medium uppercase tracking-widest text-muted-foreground">
            World
          </p>

          <h1 className="text-4xl font-bold tracking-tight text-balance">
            {world.title}
          </h1>

          <p className="max-w-2xl leading-relaxed text-muted-foreground text-pretty">
            {world.description || "No description yet — shape your world."}
          </p>
        </div>

        {/* ACTIONS RIGHT SIDE */}
        <div className="flex shrink-0 gap-3">
          {/* EDIT */}
          <Button
            asChild
            variant="outline"
            className="px-6 py-3 text-sm font-semibold"
          >
            <Link href={`/worlds/edit?worldId=${worldId}`}>Edit world</Link>
          </Button>

          {/* DELETE */}
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
                  This action is permanent. All characters, locations, and lore
                  will be removed.
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

      {/* WORLD SYSTEMS GRID ONLY (VIEW PAGE) */}
      <section className="space-y-4">
        <h2 className="text-sm font-medium uppercase tracking-widest text-muted-foreground">
          World systems
        </h2>

        <div className="grid gap-4 sm:grid-cols-2 lg:grid-cols-3">
          {systems.map((section) => {
            const slug = section.toLowerCase().replace(/ /g, "-");

            return (
              <Link key={section} href={`/worlds/${worldId}/${slug}`}>
                <Card className="group h-full cursor-pointer transition-all hover:-translate-y-0.5 hover:border-foreground/20 hover:shadow-md">
                  <CardHeader>
                    <CardTitle className="text-lg">{section}</CardTitle>
                    <CardDescription>
                      Manage {section.toLowerCase()} in this world
                    </CardDescription>
                  </CardHeader>

                  <CardContent>
                    <p className="text-sm text-muted-foreground transition-colors group-hover:text-foreground">
                      Open →
                    </p>
                  </CardContent>
                </Card>
              </Link>
            );
          })}
        </div>
      </section>
    </div>
  );
}