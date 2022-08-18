import useDropdown from "@src/hooks/useDropdown";
import Dimmer from "@src/components/@shared/Dimmer";

interface ChildrenProps {
  isDropdownOpened: boolean;
  handleOpenDropdown: () => void;
  handleCloseDropdown: () => void;
  handleToggleDropdown: () => void;
}

interface Props {
  children: ({ handleToggleDropdown }: ChildrenProps) => JSX.Element;
}

function Dropdown({ children }: Props) {
  const {
    isDropdownOpened,
    handleOpenDropdown,
    handleCloseDropdown,
    handleToggleDropdown,
  } = useDropdown();

  return (
    <>
      {isDropdownOpened && (
        <Dimmer hasBackgroundColor={false} onClick={handleCloseDropdown} />
      )}

      {children({
        isDropdownOpened,
        handleOpenDropdown,
        handleCloseDropdown,
        handleToggleDropdown,
      })}
    </>
  );
}

export default Dropdown;
