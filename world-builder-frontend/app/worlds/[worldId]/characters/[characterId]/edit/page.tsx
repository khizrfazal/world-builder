import { BackLink } from "@/components/ui/back-link";
import { Character } from "@/types/Character";
import { wbClient } from "@/utils/client";
import { Button } from "@/components/ui/button";
import { Input } from "@/components/ui/input";
import { Textarea } from "@/components/ui/textarea";
import { Label } from "@/components/ui/label";
import { redirect } from "next/navigation";

export const dynamic = "force-dynamic";

export default async function EditCharacterPage({ params }: any) {
  const { worldId, characterId } = await params;

  const character: Character = await wbClient.get(`/characters/${characterId}`);

  async function handleUpdate(formData: FormData) {
    "use server";

    const name = formData.get("name") as string;
    const summary = formData.get("summary") as string;

    await wbClient.put(`/characters/${characterId}`, {
      name,
      summary,
    });

    redirect(`/worlds/${worldId}/characters/${characterId}`);
  }

  return (
    <div className="space-y-12">
      <BackLink />

      <h1 className="text-4xl font-bold tracking-tight">Edit Character</h1>

      <form action={handleUpdate} className="space-y-6 max-w-xl">
        <div className="space-y-2">
          <Label>Name</Label>
          <Input name="name" defaultValue={character.name} required />
        </div>

        <div className="space-y-2">
          <Label>Summary</Label>
          <Textarea
            name="summary"
            defaultValue={character.summary || ""}
            placeholder="Short description of the character..."
          />
        </div>

        <div className="flex justify-center pt-4">
          <Button
            type="submit"
            className="bg-white text-black hover:bg-white/90 px-8 py-3 font-semibold border border-black"
          >
            Save Changes
          </Button>
        </div>
      </form>
    </div>
  );
}