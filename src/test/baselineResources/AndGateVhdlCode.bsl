library IEEE;
use ieee.std_logic_1164.all;
use ieee.std_logic_unsigned.all;
entity testProject is
    port (
        CLK : in std_logic
    );
end;
architecture rtl of testProject is
    signal And_1_input0 : std_logic;
    signal And_1_input1 : std_logic;
    signal And_1_output : std_logic;
begin
    And_1_output <= And_1_input0 and And_1_input1;
end;
