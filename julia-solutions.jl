### A Pluto.jl notebook ###
# v0.12.16

using Markdown
using InteractiveUtils

# ╔═╡ aec88482-35da-11eb-25b1-a79ecb5e5de4
day_1_filename = "data/input-p01-1.txt"

# ╔═╡ ec7b7fda-35e0-11eb-1ae8-a95621d60e7c
test_in = """
1721
979
366
299
675
1456
"""

# ╔═╡ dae30a88-35dc-11eb-1462-e5014a9b8cf8
function parse_input(string_content)
	map(s->parse(Int, s), string_content |> split)
end

# ╔═╡ ea62e09e-35d9-11eb-2dc6-15b112dd74e0
function read_file(filename)
	open(f -> read(f, String), filename)
end

# ╔═╡ 1b7f2118-35dc-11eb-26de-259ff6cb405a
function p1(lst)
	for a in lst, b in lst[2:end]
		if a + b == 2020
			return a * b
		end
	end
end

# ╔═╡ c66ae83c-35dc-11eb-10a1-d9673f824e85
p1(parse_input(test_in))

# ╔═╡ 3dd20472-35e0-11eb-1d68-5b65741c2c96
p1(read_file(day_1_filename) |> parse_input)

# ╔═╡ 082ce912-35e1-11eb-2079-b1361408504f
function p2(lst)
	for a in lst, b in lst[2:end], c in lst[3:end]
		if a + b + c == 2020
			return *(a, b, c)
		end
	end
end

# ╔═╡ c4b87b08-35e1-11eb-30b3-4b2a6a947564
p2(read_file(day_1_filename) |> parse_input)

# ╔═╡ Cell order:
# ╠═aec88482-35da-11eb-25b1-a79ecb5e5de4
# ╠═ec7b7fda-35e0-11eb-1ae8-a95621d60e7c
# ╠═dae30a88-35dc-11eb-1462-e5014a9b8cf8
# ╠═ea62e09e-35d9-11eb-2dc6-15b112dd74e0
# ╠═1b7f2118-35dc-11eb-26de-259ff6cb405a
# ╠═c66ae83c-35dc-11eb-10a1-d9673f824e85
# ╠═3dd20472-35e0-11eb-1d68-5b65741c2c96
# ╠═082ce912-35e1-11eb-2079-b1361408504f
# ╠═c4b87b08-35e1-11eb-30b3-4b2a6a947564
