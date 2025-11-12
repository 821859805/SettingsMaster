import { create } from 'zustand';

interface NamespaceState {
  namespace: string;
  setNamespace: (namespace: string) => void;
}

export const useNamespaceStore = create<NamespaceState>((set) => ({
  namespace: 'default',
  setNamespace: (namespace) => set({ namespace })
}));
