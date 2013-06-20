begin
	! Ett enkelt program som testar;
	! inre block;
	integer I; integer K;
	I:=2;
	K:=3;
	begin
		integer I;
		I:=4;
		K:=I+K;
	end;
	I:=K*I;
end
