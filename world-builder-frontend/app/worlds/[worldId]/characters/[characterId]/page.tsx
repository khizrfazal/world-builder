import { BackLink } from "@/components/ui/back-link";
import { Character } from "@/types/Character";
import { wbClient } from "@/utils/client";
import { Button } from "@/components/ui/button";
import Link from "next/link";
import {
  AlertDialog,
  AlertDialogTrigger,
  AlertDialogContent,
  AlertDialogHeader,
  AlertDialogTitle,
  AlertDialogDescription,
  AlertDialogFooter,
  AlertDialogCancel,
} from "@/components/ui/alert-dialog";
import { redirect } from "next/navigation";

export const dynamic = "force-dynamic";

export default async function CharacterDetailPage({ params }: any) {
  const { worldId, characterId } = await params;

  const character: Character = await wbClient.get(`/characters/${characterId}`);

  const handleDelete = async () => {
    "use server";
    await wbClient.delete(`/characters/${characterId}`);
    redirect(`/worlds/${worldId}/characters`);
  };

  return (
    <div className="space-y-12">
      <BackLink />

      {/* HEADER */}
      <header className="flex flex-col gap-6 sm:flex-row sm:items-start sm:justify-between">
        <div className="space-y-3">
          <h1 className="text-4xl font-bold tracking-tight">
            {character.name}
          </h1>

          <p className="max-w-2xl leading-relaxed text-muted-foreground">
            {character.summary || "No summary provided."}
          </p>
        </div>

        <div className="flex gap-3">
          {/* EDIT BUTTON */}
          <Button
            asChild
            variant="outline"
            className="px-6 py-3 text-sm font-semibold"
          >
            <Link href={`/worlds/${worldId}/characters/${characterId}/edit`}>
              Edit Character
            </Link>
          </Button>

          {/* DELETE BUTTON */}
          <AlertDialog>
            <AlertDialogTrigger asChild>
              <Button className="px-6 py-3 text-sm font-semibold bg-red-600 text-white hover:bg-red-600 cursor-pointer">
                Delete Character
              </Button>
            </AlertDialogTrigger>
            <AlertDialogContent>
              <AlertDialogHeader>
                <AlertDialogTitle>Delete this character?</AlertDialogTitle>
                <AlertDialogDescription>
                  This action is permanent. This character will be removed from your world.
                </AlertDialogDescription>
              </AlertDialogHeader>

              <AlertDialogFooter>
                <AlertDialogCancel>Cancel</AlertDialogCancel>

                <form action={handleDelete}>
                  <Button
                    type="submit"
                    className="bg-red-600 text-white hover:bg-red-600 cursor-pointer"
                  >
                    Delete
                  </Button>
                </form>
              </AlertDialogFooter>
            </AlertDialogContent>
          </AlertDialog>
        </div>
      </header>
    </div>
  );
}