DELIMITER $$
CREATE DEFINER=`root`@`localhost` FUNCTION `kondal_mask`(v_chars TEXT) RETURNS text CHARSET utf8mb4
    NO SQL
    SQL SECURITY INVOKER
BEGIN
    DECLARE v_retval TEXT DEFAULT '';
    DECLARE u_pos    INT UNSIGNED;
    DECLARE u        INT UNSIGNED;
	DECLARE m        INT UNSIGNED;
	Declare xx nvarchar(50);
	DECLARE length INT UNSIGNED;

	SET xx = '';

    SET u = LENGTH(v_chars);
    SET length = u;
    SET m = CEIL(u / 2);
	
    WHILE length > m
		DO
			SET xx = CONCAT(xx, 'X');
            SET length = length -1;
	END WHILE;
    
    SET v_retval = CONCAT(xx, RIGHT(v_chars, m));

    RETURN v_retval;
END$$
DELIMITER ;
