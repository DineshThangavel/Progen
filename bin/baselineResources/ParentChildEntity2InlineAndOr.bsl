library IEEE;
use ieee.std_logic_1164.all;
use ieee.std_logic_unsigned.all;
entity entity2_2 is
    port (
        entity2_2_input2 : in std_logic;
        entity2_2_input1 : in std_logic;
        entity2_2_input0 : in std_logic;
        entity2_2_output : out std_logic
    );
end;
architecture rtl of entity2_2 is
    signal andGateTest_2_1_input0 : std_logic;
    signal andGateTest_2_1_input1 : std_logic;
    signal andGateTest_2_1_output : std_logic;
    signal andGateTest_2_2_input0 : std_logic;
    signal andGateTest_2_2_input1 : std_logic;
    signal andGateTest_2_2_output : std_logic;
begin
    andGateTest_2_1_output <= andGateTest_2_1_input0 and andGateTest_2_1_input1;
    andGateTest_2_2_output <= andGateTest_2_2_input0 and andGateTest_2_2_input1;
    andGateTest_2_2_input1 <= entity2_2_input2;
    andGateTest_2_1_input1 <= entity2_2_input1;
    andGateTest_2_1_input0 <= entity2_2_input0;
    andGateTest_2_2_input0 <= andGateTest_2_1_output;
    entity2_2_output <= andGateTest_2_2_output;
end;
