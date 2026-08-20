import { updateWorld } from "@/actions/worldActions";
import EditWorldForm from "@/components/EditWorldForm";
import { serverClient } from "@/utils/server-client";
import { cookies } from "next/headers";
import { BackLink } from "@/components/ui/back-link";

export default async function EditWorldPage({ params }: any) {
  const { worldId } = await params;
  const cookieStore = await cookies();
  const token = cookieStore.get("token")?.value ?? null;
  const world = await serverClient.get(`/worlds/${worldId}`, token);

  async function handleUpdate(formData: FormData) {
    "use server";
    await updateWorld(worldId, {
      title: formData.get("title") as string,
      description: formData.get("description") as string,
    });
  }

  return (
    <div className="space-y-10 max-w-2xl mx-auto">
      <BackLink />
      <EditWorldForm world={world} action={handleUpdate} />
    </div>
  );
}