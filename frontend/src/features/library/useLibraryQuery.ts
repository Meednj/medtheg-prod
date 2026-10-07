import { useQuery } from "@tanstack/react-query";
import { getLibrary } from "../../api/library";
import { queryKeys } from "../../api/queryKeys";

export function useLibraryQuery() {
  return useQuery({
    queryKey: queryKeys.library,
    queryFn: getLibrary,
  });
}
