import { ReactNode } from "react";

interface DataGridContainerProps {
  children?: ReactNode,
}

export default function DataGridContainer(props: DataGridContainerProps) {
  return (
    <div style={{height: "400px", marginBottom: "12px" }}>
      {props.children}
    </div>
  )
}