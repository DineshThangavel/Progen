library IEEE;
use ieee.std_logic_1164.all;
use ieee.std_logic_unsigned.all;
entity testProject is
    port (
        CLK : in std_logic
    );
end;
architecture rtl of testProject is
    component entity1_1
        port (
            entity1_1_input2 : in std_logic;
            entity1_1_input1 : in std_logic;
            entity1_1_input0 : in std_logic;
            entity1_1_output : out std_logic
        );
    end component;
    signal entity1_1_input2 : std_logic;
    signal entity1_1_input1 : std_logic;
    signal entity1_1_input0 : std_logic;
    signal entity1_1_output : std_logic;
    component entity2_2
        port (
            entity2_2_input2 : in std_logic;
            entity2_2_input1 : in std_logic;
            entity2_2_input0 : in std_logic;
            entity2_2_output : out std_logic
        );
    end component;
    signal entity2_2_input2 : std_logic;
    signal entity2_2_input1 : std_logic;
    signal entity2_2_input0 : std_logic;
    signal entity2_2_output : std_logic;
begin
    entity1_1 : entity1_1
        port map (
            entity1_1_input2 => entity1_1_input2,
            entity1_1_input1 => entity1_1_input1,
            entity1_1_input0 => entity1_1_input0,
            entity1_1_output => entity1_1_output
        );
    entity2_2_input0 <= entity1_1_output;
    entity2_2 : entity2_2
        port map (
            entity2_2_input2 => entity2_2_input2,
            entity2_2_input1 => entity2_2_input1,
            entity2_2_input0 => entity2_2_input0,
            entity2_2_output => entity2_2_output
        );
end;
