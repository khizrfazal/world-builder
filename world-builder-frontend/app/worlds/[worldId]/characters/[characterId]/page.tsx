import { BackLink } from "@/components/ui/back-link";
import { Character } from "@/types/Character";
import { cookies } from "next/headers";
import { serverClient } from "@/utils/server-client";
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

  const cookieStore = await cookies();
  const token = cookieStore.get("token")?.value ?? null;

  const character: Character = await serverClient.get(
    `/characters/${characterId}`,
    token
  );

  const handleDelete = async () => {
    "use server";

    const cookieStore = await cookies();
    const token = cookieStore.get("token")?.value ?? null;

    await serverClient.delete(`/characters/${characterId}`, token);
    redirect(`/worlds/${worldId}/characters`);
  };

  return (
    <div className="space-y-12">
      <BackLink />

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
          <Button
            asChild
            variant="outline"
            className="px-6 py-3 text-sm font-semibold"
          >
            <Link href={`/worlds/${worldId}/characters/${characterId}/edit`}>
              Edit Character
            </Link>
          </Button>

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