library IEEE;
use ieee.std_logic_1164.all;
use ieee.std_logic_unsigned.all;
entity entity1_1 is
    port (
        entity1_1_input2 : in std_logic;
        entity1_1_input1 : in std_logic;
        entity1_1_input0 : in std_logic;
        entity1_1_output : out std_logic
    );
end;
architecture rtl of entity1_1 is
    component orGateTest_1_1
        port (
            orGateTest_1_1_input0 : in std_logic;
            orGateTest_1_1_input1 : in std_logic;
            orGateTest_1_1_output : out std_logic
        );
    end component;
    signal orGateTest_1_1_input0 : std_logic;
    signal orGateTest_1_1_input1 : std_logic;
    signal orGateTest_1_1_output : std_logic;
    signal orGateTest_1_2_input0 : std_logic;
    signal orGateTest_1_2_input1 : std_logic;
    signal orGateTest_1_2_output : std_logic;
begin
    orGateTest_1_1 : orGateTest_1_1
        port map (
            orGateTest_1_1_input0 => orGateTest_1_1_input0,
            orGateTest_1_1_input1 => orGateTest_1_1_input1,
            orGateTest_1_1_output => orGateTest_1_1_output
        );
    orGateTest_1_2_output <= orGateTest_1_2_input0 or orGateTest_1_2_input1;
    orGateTest_1_2_input1 <= entity1_1_input2;
    orGateTest_1_1_input1 <= entity1_1_input1;
    orGateTest_1_1_input0 <= entity1_1_input0;
    orGateTest_1_2_input0 <= orGateTest_1_1_output;
    entity1_1_output <= orGateTest_1_2_output;
end;
