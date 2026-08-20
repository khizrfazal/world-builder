import { BackLink } from "@/components/ui/back-link";
import { Button } from "@/components/ui/button";
import { Input } from "@/components/ui/input";
import { Textarea } from "@/components/ui/textarea";
import { Label } from "@/components/ui/label";
import { cookies } from "next/headers";
import { serverClient } from "@/utils/server-client";
import { redirect } from "next/navigation";

export const dynamic = "force-dynamic";

export default async function CreateCharacterPage({ params }: any) {
  const { worldId } = await params;

  async function handleCreate(formData: FormData) {
    "use server";

    const cookieStore = await cookies();
    const token = cookieStore.get("token")?.value ?? null;

    const name = formData.get("name") as string;
    const summary = formData.get("summary") as string;

    await serverClient.post(
      `/worlds/${worldId}/characters`,
      { name, summary },
      token
    );

    redirect(`/worlds/${worldId}/characters`);
  }

  return (
    <div className="space-y-10">
      <BackLink />

      <h1 className="text-3xl font-bold tracking-tight">Create Character</h1>

      <form action={handleCreate} className="space-y-6 max-w-xl">
        <div className="space-y-2">
          <Label>Name</Label>
          <Input name="name" required />
        </div>

        <div className="space-y-2">
          <Label>Summary</Label>
          <Textarea
            name="summary"
            placeholder="Short description of the character..."
          />
        </div>

        <div className="flex justify-center pt-4">
          <Button
            type="submit"
            className="bg-white text-black hover:bg-white/90 px-8 py-3 font-semibold border border-black"
          >
            Create Character
          </Button>
        </div>
      </form>
    </div>
  );
}