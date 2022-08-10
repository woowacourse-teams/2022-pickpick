import useDropdown from "@src/hooks/useDropdown";
import Dimmer from "../@shared/Dimmer";

interface ChildrenProps {
  isDropdownOpened: boolean;
  handleToggleDropdown: () => void;
}

interface Props {
  children: ({ handleToggleDropdown }: ChildrenProps) => JSX.Element;
}

function Dropdown({ children }: Props) {
  const {
    isDropdownOpened: isDropdownOpened,
    handleCloseDropdown: handleCloseDropdown,
    handleToggleDropdown: handleToggleDropdown,
  } = useDropdown();

  return (
    <>
      {isDropdownOpened && (
        <Dimmer hasBackgroundColor={false} onClick={handleCloseDropdown} />
      )}
      {children({ isDropdownOpened, handleToggleDropdown })}
    </>
  );
}

export default Dropdown;
