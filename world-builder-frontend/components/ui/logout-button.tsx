"use client";

import { logout } from "@/actions/userActions";
import { Button } from "@/components/ui/button";

export function LogoutButton() {
  async function handleLogout() {
    await logout();
    window.location.href = "/login";
  }

  return (
    <Button
      onClick={handleLogout}
      className="px-6 py-3 font-semibold bg-black text-white hover:bg-black/90"
    >
      Logout
    </Button>
  );
}
