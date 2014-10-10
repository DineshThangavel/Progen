library IEEE;
use ieee.std_logic_1164.all;
use ieee.std_logic_unsigned.all;
entity testProject is
    port (
        CLK : in std_logic
    );
end;
architecture rtl of testProject is
    signal Or_1_input0 : std_logic;
    signal Or_1_input1 : std_logic;
    signal Or_1_output : std_logic;
    signal And_2_input0 : std_logic;
    signal And_2_input1 : std_logic;
    signal And_2_output : std_logic;
begin
    Or_1_output <= Or_1_input0 or Or_1_input1;
    And_2_input0 <= Or_1_output;
    And_2_output <= And_2_input0 and And_2_input1;
end;
