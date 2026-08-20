import { BackLink } from "@/components/ui/back-link";
import { cookies } from "next/headers";
import { serverClient } from "@/utils/server-client";
import { redirect } from "next/navigation";
import NewWorldForm from "@/components/NewWorldForm";

export const dynamic = "force-dynamic";

export default async function NewWorldPage() {
  async function handleCreate(formData: FormData) {
    "use server";

    const cookieStore = await cookies();
    const token = cookieStore.get("token")?.value ?? null;

    const title = formData.get("title") as string;
    const description = formData.get("description") as string;

    await serverClient.post(
      "/worlds",
      { title, description },
      token
    );

    redirect("/worlds");
  }

  return (
    <div className="space-y-10 max-w-2xl mx-auto">
      <BackLink />

      <header className="space-y-2">
        <h1 className="text-3xl font-bold tracking-tight">Create a New World</h1>
        <p className="text-muted-foreground text-sm">
          Start building a universe for your stories, characters, and ideas.
        </p>
      </header>

      <NewWorldForm action={handleCreate} />
    </div>
  );
}